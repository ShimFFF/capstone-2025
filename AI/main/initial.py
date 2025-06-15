from faster_whisper import WhisperModel

model_size = "large-v3"

# 모델 로드 시간 체크
import time

start_time = time.time()
model = WhisperModel(model_size, device="cpu", compute_type="int8")
end_time = time.time()
print(f"모델 로드 시간: {end_time - start_time:.2f}초")
