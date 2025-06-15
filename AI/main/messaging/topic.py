from utils.logging_config import configure_logging

from enum import Enum
import os
import sys
import boto3
import botocore

logger = configure_logging(__name__)
PROFILE = os.getenv("PROFILE", "prod")
AWS_REGION = os.getenv("AWS_REGION")

logger.info("AWS_REGION: %s", AWS_REGION)


class AnalysisTopic(Enum):
    ANALYSIS_REQUEST = "request_analysis"
    ANALYSIS_RESPONSE = "response_analysis"


def get_topic(default_topic: str) -> str:
    """
    env_var_name에 해당하는 환경 변수 값이 존재하면 그 값을 사용하고,
    그렇지 않으면 default_topic을 반환합니다_
    """

    if PROFILE == "prod":
        return get_sqs_queue_url(default_topic)
    else:
        return default_topic


def get_sqs_queue_url(topic: str):
    session = boto3.Session()
    client = session.client("sqs", region_name=AWS_REGION)

    try:
        response = client.get_queue_url(QueueName=topic)
    except botocore.exceptions.ClientError as err:
        if err.response["Error"]["Code"] == "AWS.SimpleQueueService.NonExistentQueue":
            print(f"Queue {topic} does not exist")
            sys.exit(1)
        else:
            raise

    queue_url = response["QueueUrl"]
    logger.info(f"topic: {topic} / Queue URL: {queue_url}")
    return queue_url
