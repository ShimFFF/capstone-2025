# Git Flow

## Main
❗프로덕션 배포 가능한 상태 유지  
❗직접 작업 금지  
❗develop 브랜치에서 merge를 통해 업데이트  

<br>

## Develop
❗개발의 기본 브랜치, 모든 기능이 merge 되는 브랜치  
❗직접 배포 금지  
❗다음 릴리즈를 준비하는 작업은 모두 develop에서  

<br>

## Feature
❗새로운 기능이나 변경 사항을 개발  
❗이름 형식: feature/#이슈번호-기능명  
❗브랜치 상태 공유를 위해 주기적으로 develop rebase 하거나 병합  

<br>

## Release
❗릴리즈 준비(버그 수정, 문서화)를 위해  
❗이름 형식: release/#이슈번호-버그명  
❗main 브랜치에서 분기되며, 준비가 완료되면 main과 develop에 merge  
❗QA 및 테스트 작업에 사용

<br>

## Hotfix
❗프로덕션에서 발생한 긴급 버그 수정  
❗이름 형식: hotfix/#이슈번호-버그명  
❗main 브랜치에서 분기되며, 수정 완료 후 main과 develop에 merge  
❗빠르게 프로덕션에 배포하기 위해 사용  


---

# Flow

### 새 기능 개발

1. repo에서 이슈 생성
2. develop 브랜치에서 feature 브랜치 생성

```bash
git checkout develop
git checkout -b feature/#이슈번호-기능명...
```

1. 작업 완료 후, develop 브랜치로 merge
2. 작업 완료 후 feature 브랜치 삭제

### 긴급 버그 수정

1. repo에서 이슈 생성
2. main 브랜치에서 hotfix 브랜치 생성

```bash
git checkout main
git checkout -b hotfix/#이슈번호-버그명..
```

1. 버그 수정 후, main과 develop 브랜치 병합
2. hotfix 브랜치 삭제
