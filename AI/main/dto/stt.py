from pydantic import BaseModel


class STTResponse(BaseModel):
    voiceFileId: int
    analysisResultStatus: str = "SUCCESS"
    sttContent: str
