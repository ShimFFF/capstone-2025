package naeilmolae.domain.voicefile.application.evnets;


public record VoiceFileAnalysisEvent(Long voiceFileId, String fileUrl, String content) {
}
