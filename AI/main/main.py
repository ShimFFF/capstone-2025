from messaging.dependencies import get_producer, get_consumer
from messaging.topic import get_topic, AnalysisTopic
from utils.logging_config import configure_logging
from dto.stt import STTResponse

from faster_whisper import WhisperModel
import os
import boto3
from pydub import AudioSegment, silence
import requests
import asyncio

logger = configure_logging(__name__)

ANALYSIS_REQUEST = get_topic(AnalysisTopic.ANALYSIS_REQUEST.value)
ANALYSIS_RESPONSE = get_topic(AnalysisTopic.ANALYSIS_RESPONSE.value)

logger.info(f"ANALYSIS_REQUEST: {ANALYSIS_REQUEST}")
logger.info(f"ANALYSIS_RESPONSE: {ANALYSIS_RESPONSE}")

# 환경변수 읽기
AWS_ACCESS_KEY_ID = os.getenv("AWS_ACCESS_KEY_ID")
AWS_SECRET_ACCESS_KEY = os.getenv("AWS_SECRET_ACCESS_KEY")
AWS_REGION = os.getenv("AWS_REGION")


def trim_silence(
    audio: AudioSegment, silence_thresh: int = -40, min_silence_len: int = 500
) -> AudioSegment:
    """
    오디오 앞뒤의 무음 구간을 잘라냅니다.
    - silence_thresh: 무음으로 간주할 dBFS 임계치 (default: -40dB)
    - min_silence_len: 최소 무음 길이(ms) (default: 500ms)
    """
    non_silent = silence.detect_nonsilent(
        audio, min_silence_len=min_silence_len, silence_thresh=silence_thresh
    )
    if not non_silent:
        return audio
    start_ms = non_silent[0][0]
    end_ms = non_silent[-1][1]
    return audio[start_ms:end_ms]


class AudioProcessor:
    def __init__(self, s3_client):
        self.s3_client = s3_client
        model_size = "large-v3"
        self.model = WhisperModel(model_size, device="cpu", compute_type="int8")

    def stt(self, filepath):
        """
        faster_whisper를 이용하여 음성 파일을 텍스트로 변환하는 메서드.
        """
        try:
            segments, info = self.model.transcribe(
                filepath, beam_size=5, vad_filter=True
            )
            logger.info(
                "Detected language '%s' with probability %f"
                % (info.language, info.language_probability)
            )
            stt_result = ""
            for segment in segments:
                # 무음 확률 필터링
                if getattr(segment, "no_speech_prob", 0) > 0.5:
                    continue
                logger.info(
                    "[%.2fs -> %.2fs] %s" % (segment.start, segment.end, segment.text)
                )
                stt_result += segment.text
            return stt_result.strip()
        except Exception as e:
            raise RuntimeError(f"STT 처리 중 오류 발생: {e}")

    def get_wav_file_from_s3(self, file_url):
        try:
            base_dir = os.path.join(os.path.dirname(__file__), "music")
            os.makedirs(base_dir, exist_ok=True)
            local_wav_path = os.path.join(base_dir, os.path.basename(file_url))
            if file_url.startswith("s3://"):
                bucket_name, key = self.parse_s3_url(file_url)
                self.s3_client.download_file(bucket_name, key, local_wav_path)
            else:
                self.download_file_from_https(file_url, local_wav_path)

            # pydub로 로드 후 무음 제거 & 리샘플링
            audio = AudioSegment.from_file(local_wav_path, format="wav")
            trimmed = trim_silence(audio, silence_thresh=-50, min_silence_len=300)
            resampled = trimmed.set_frame_rate(16000)
            converted_wav_path = local_wav_path.replace(".wav", "_16k.wav")
            resampled.export(converted_wav_path, format="wav")
            return converted_wav_path
        except Exception as e:
            raise RuntimeError(f"S3에서 파일을 가져오는 중 오류 발생: {e}")

    @staticmethod
    def download_file_from_https(url, local_path):
        response = requests.get(url)
        response.raise_for_status()
        with open(local_path, "wb") as f:
            f.write(response.content)

    @staticmethod
    def parse_s3_url(s3_url):
        if not s3_url.startswith("s3://"):
            raise ValueError("올바른 S3 URL이 아닙니다.")
        parts = s3_url[5:].split("/", 1)
        if len(parts) != 2:
            raise ValueError("S3 URL 형식이 올바르지 않습니다.")
        return parts[0], parts[1]


class MessageProcessor:
    def __init__(self, audio_processor):
        self.audio_processor = audio_processor

    def process_message(self, msg) -> dict:
        try:
            file_url = msg.get("fileUrl")
            wav_file_path = self.audio_processor.get_wav_file_from_s3(file_url)

            stt_text = self.audio_processor.stt(wav_file_path)
            logger.info(f"STT 결과: {stt_text}")

            return STTResponse(
                voiceFileId=int(msg.get("voiceFileId")),
                analysisResultStatus="SUCCESS",
                sttContent=stt_text,
            )

        except Exception as e:
            logger.error(f"메시지 처리 중 오류 발생: {e}")
            return STTResponse(
                voiceFileId=int(msg.get("voiceFileId")),
                analysisResultStatus="ERROR",
                sttContent=str(e),
            )
            
class Analyzer:
    @staticmethod
    def check_similarity(text1, text2, threshold=0.85):
        similarity = difflib.SequenceMatcher(None, text1, text2).ratio()
        is_similar = similarity >= threshold
        return is_similar, similarity

    @staticmethod
    def detect_inappropriate_content(text):
        # TODO: 부적절한 내용 탐지 로직 구현
        # 예: inappropriate_keywords = ["금지된 단어1", "금지된 단어2"]
        # for keyword in inappropriate_keywords:
        #     if keyword in text:
        #         return True
        return False

async def main():
    producer = get_producer()
    consumer = get_consumer(ANALYSIS_REQUEST)
    await consumer.start()
    await producer.start()

    s3_client = boto3.client(
        "s3",
        aws_access_key_id=AWS_ACCESS_KEY_ID,
        aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
        region_name=AWS_REGION,
    )

    audio_processor = AudioProcessor(s3_client)
    message_processor = MessageProcessor(audio_processor)

    try:
        async for msg in consumer.consume_messages():
            key = msg.get("voiceFileId")
            logger.info(f"분석 요청: {msg}")

            message = message_processor.process_message(msg)
            logger.info(f"Id: {key}, Message: {message.model_dump()}")

            await producer.send_message(ANALYSIS_RESPONSE, message.model_dump())
    finally:
        await consumer.stop()


if __name__ == "__main__":
    asyncio.run(main())
