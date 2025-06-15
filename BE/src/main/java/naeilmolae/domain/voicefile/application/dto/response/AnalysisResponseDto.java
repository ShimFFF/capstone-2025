package naeilmolae.domain.voicefile.application.dto.response;

public record AnalysisResponseDto(Long voiceFileId, String analysisResultStatus, String sttContent) {
}