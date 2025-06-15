package naeilmolae.domain.voicefile.application.evnets;

public interface VoiceFileEventListener {
    void handleEvent(VoiceFileAnalysisEvent event);
}
