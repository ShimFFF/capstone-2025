import logging
import os


def configure_logging(file_path):
    """
    로깅 설정 함수. 파일 이름에 따라 콘솔 + 파일 핸들러를 동적으로 추가합니다.
    :param file_path: 호출 파일 경로 (__file__ 등)
    """
    # 로거 이름으로 사용할 모듈명 추출
    module_name = os.path.splitext(os.path.basename(file_path))[0]

    # 로그 디렉토리 설정 및 생성
    log_dir = "logs"
    os.makedirs(log_dir, exist_ok=True)

    # ─────────────────────────────────────────────────────────
    # 1) 콘솔 핸들러 (DEBUG 이상)
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)
    console_fmt = "%(asctime)s - %(name)s - %(levelname)s - %(message)s [PID:%(process)d, TID:%(thread)d]"
    console_handler.setFormatter(logging.Formatter(console_fmt))

    # 2) 파일 핸들러 (INFO 이상)
    log_file = os.path.join(log_dir, f"{module_name}.log")
    file_handler = logging.FileHandler(log_file, encoding="utf-8")
    file_handler.setLevel(logging.INFO)
    file_fmt = "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
    file_handler.setFormatter(logging.Formatter(file_fmt))
    # ─────────────────────────────────────────────────────────

    # 로거 설정
    logger = logging.getLogger(module_name)
    logger.setLevel(logging.DEBUG)
    # 중복 핸들러 추가 방지
    if not logger.handlers:
        logger.addHandler(console_handler)
        logger.addHandler(file_handler)

    return logger
