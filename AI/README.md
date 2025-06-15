# 서비스 소개
STT (Speech-to-Text) 변환
사용자의 음성 파일(WAV 형식)을 텍스트로 변환합니다.
Whisper.cpp 기반으로 한국어 음성을 텍스트로 변환하는 기능을 제공합니다.
텍스트 유사도 검증
두 텍스트 간 유사도를 계산합니다.
difflib.SequenceMatcher를 사용하여 유사도를 측정하며, 설정된 임계값(threshold) 이상인 경우 두 문장이 유사하다고 판단합니다.
WAV 파일 변환
음성 파일의 샘플링 레이트를 16kHz로 변환합니다.
Pydub 라이브러리를 활용하여 WAV 파일의 품질을 표준화합니다.

## 초기 설정
- `초기 모델 다운로드`:
Docker 컨테이너 내부에서는 Hugging Face에서 모델을 다운로드하는 속도가 느리므로, 서비스 시작 전에 `main/initial.py` 스크립트를 실행하여 모델 파일을 미리 다운로드 받습니다.

- `실행 순서`:
먼저 `main/initial.py`를 실행하여 모델 다운로드 및 변환을 완료한 후, docker-compose.yml(예시는 카톡주세요)을 실행해 서비스가 정상적으로 동작하도록 합니다.

- `캐시 볼륨 마운트`:
README에 명시된 대로 로컬의 `~/.cache/huggingface/hub` 디렉토리를 컨테이너 내부의 `/root/.cache/huggingface/hub`로 마운트하여, 동일한 모델 파일을 재다운로드하지 않고 캐시를 재사용합니다.

## 주요 기능
- `모델 기반 음성 인식`:
환경변수 MODEL_SIZE를 통해 모델 크기를 설정할 수 있으며, 기본적으로 large-v3 모델을 사용합니다.
- `Kafka 연동`:
Kafka를 통해 음성 파일 분석 요청과 응답 메시지를 주고받습니다.
- `AWS S3 연동`:
AWS S3에서 음성 파일을 다운로드하여 처리합니다.
- `Hugging Face Hub 캐시`:
모델 파일 등 Hugging Face Hub에서 다운로드한 파일을 로컬 캐시에 저장합니다.

## Prerequisites
- Docker 및 Docker Compose 설치
- 외부 Kafka 네트워크 (여기서는 kafka_network로 구성)
    - Kafka 브로커 서비스가 이 네트워크에 있어야 합니다.
- AWS 자격 증명 (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`)
- (선택 사항) Hugging Face Hub 캐시 디렉토리(`~/.cache/huggingface/hub`)가 존재해야 합니다.

## 환경 변수
docker-compose 파일 내에 아래와 같은 환경 변수를 설정합니다:

- `MODEL_SIZE`: 사용 모델의 크기 (예: large-v3)
- `PROFILE`: 실행 프로파일 (예: test)
- `KAFKA_BOOTSTRAP_SERVER_HOST`: Kafka 브로커의 호스트 이름 (예: morae_kafka)
- `KAFKA_BOOTSTRAP_SERVER_PORT`: Kafka 브로커의 포트 (예: 9094)
- `AWS_ACCESS_KEY_ID`: AWS 자격 증명 ID
- `AWS_SECRET_ACCESS_KEY`: AWS 비밀 액세스 키
- `AWS_REGION`: AWS 리전 (예: ap-northeast-2)

## 볼륨 마운트
컨테이너 재실행 시에도 Hugging Face Hub 캐시가 유지되도록, 로컬의 `~/.cache/huggingface/hub` 디렉토리를 컨테이너 내부의 `/root/.cache/huggingface/hub`로 마운트합니다.

```
volumes:
  - ~/.cache/huggingface/hub:/root/.cache/huggingface/hub
```
Note: Docker 컨테이너의 기본 사용자(root)를 기준으로 캐시 경로가 /root/.cache/huggingface/hub가 됩니다.


## 참고 사항
- `모델 로딩`:
모델 로딩은 시간이 다소 걸릴 수 있으므로, 초기 로딩 시 로그를 주의 깊게 확인하세요.
- `네트워크 및 타임아웃 최적화`:
Docker 환경에서는 네트워크 설정, 타임아웃, 리소스 제한 등을 적절히 조정하여 안정적인 동작을 보장해야 합니다.
- `캐시 유지`:
캐시 볼륨을 마운트하면, 동일한 모델 파일이나 데이터를 재다운로드하지 않아 빠른 응답성을 유지할 수 있습니다.
