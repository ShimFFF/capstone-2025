package naeilmolae.domain.voicefile.application.evnets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.application.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.application.usecase.VoiceFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!prod")
public class KafkaAnalysisRequest implements VoiceFileEventListener {

    private final VoiceFileUseCase voiceFileUseCase;
    private final StreamBridge streamBridge;

    @Value("${kafka.topic.analysis.request}")
    private String topic;

    @Override
    @EventListener
    @Transactional
    public void handleEvent(VoiceFileAnalysisEvent event) {
        log.info("VoiceFileAnalysisEvent received: {}", event.voiceFileId());
        VoiceFile voiceFile = voiceFileUseCase.findById(event.voiceFileId());
        voiceFile.prepareAnalysis();

        AnalysisRequestDto analysisRequestDto = new AnalysisRequestDto(
                event.voiceFileId(),
                event.fileUrl(),
                event.content()
        );

        boolean sent = streamBridge.send(topic, MessageBuilder.withPayload(analysisRequestDto)
                .build());

        if (sent) {
            log.info("Sent message via StreamBridge: {}", analysisRequestDto);
        } else {
            log.error("Failed to send message via StreamBridge");
        }
    }
}
