FROM python:3.11-slim

WORKDIR /app

# ffmpeg 설치: apt-get 업데이트 후 ffmpeg 설치, 캐시 삭제
COPY requirements.txt /app
RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*
RUN pip install --no-cache-dir -r requirements.txt

COPY . /app

