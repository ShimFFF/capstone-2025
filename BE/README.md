# 서비스 설명
이 저장소는 **캡스톤 프로젝트의 백엔드(API 서버)** 코드입니다.

## 실행 방법
workflow에 있는 키 값들을 만들어서 넣어줍니다.
단 FIREBASE_SERVICE_ACCOUNT의 경우 암호화된 값으로 넣어줍니다.

AWS에서 EB를 만들고 IAM 

1. Github Actions용 Secret 환경 변수 설정

`workflow` 또는 `Dockerfile`을 사용하여 배포 시, 아래 환경 변수를 `GitHub Secrets`에 등록해야 합니다.

| 변수명 | 설명 |
|--------|------|
| `AWS_ACCESS_KEY_ID` | AWS IAM 사용자 키 |
| `AWS_SECRET_ACCESS_KEY` | AWS IAM 사용자 시크릿 |
| `AWS_REGION` | AWS 리전 (`ap-northeast-2`) |
| `AWS_ECR_REPO` | ECR 리포지토리 주소 |
| `EB_APP_NAME` | Elastic Beanstalk 앱 이름 (`api-server`) |
| `EB_ENV_NAME` | Elastic Beanstalk 환경 이름 (`Api-server-env-1`) |
| `DOCKER_COMPOSE_PROD` | Docker Compose 프로덕션 구성 (base64 암호화) |
| `FIREBASE_SERVICE_ACCOUNT` | Firebase Admin SDK 서비스 키 (base64 암호화) |
| `YML` | 배포 시 사용할 yml 파일 (base64 암호화) |

> 🔐 `FIREBASE_SERVICE_ACCOUNT`, `DOCKER_COMPOSE_PROD`, `YML`은 보안상 **암호화된 문자열 (base64)** 로 저장하며, `decode 후 사용`됩니다.


### 2. Firebase Admin SDK 설정

- `FIREBASE_SERVICE_ACCOUNT`는 Firebase Console → 서비스 계정 → 키 발급으로 얻은 JSON 파일을 base64 인코딩하여 넣습니다.
- 예:
  ```bash
  cat firebase-key.json | base64
  ```
3. AWS EB 배포 환경 구성
IAM 사용자에 EB, ECR, EC2, S3 권한 부여

EB 앱 및 환경 생성:
```
bash
eb init -p docker api-server
eb create Api-server-env-1
```
또는 ECR 이미지 → EB 배포 시, docker-compose.yml 기반으로 구성

```yml
version: '3'
services:
  api:
    image: 060795914361.dkr.ecr.ap-northeast-2.amazonaws.com/naeilmolae:latest
    ports:
      - "80:8081"
    restart: always
}
```
