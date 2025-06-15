from faster_whisper import WhisperModel

model_size = "large-v3"

# 모델 로드 시간 체크
import time

start_time = time.time()
model = WhisperModel(model_size, device="cpu", compute_type="int8")
end_time = time.time()
print(f"모델 로드 시간: {end_time - start_time:.2f}초")

filenames = [
    "1.wav",
    "2.wav",
    "3.wav",
    "4.wav",
]

for filename in filenames:
    segments, info = model.transcribe(
        f"/Users/mousebook/Documents/side project/2024hack/stt-server/src/test/music/{filename}",
        beam_size=5,
    )

    print(
        "Detected language '%s' with probability %f"
        % (info.language, info.language_probability)
    )

    stt_result = ""

    for segment in segments:
        print("[%.2fs -> %.2fs] %s" % (segment.start, segment.end, segment.text))
        stt_result += segment.text

    print(stt_result)
