package naeilmolae.domain.voicefile.application.evnets;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.application.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.application.usecase.VoiceFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class SqsAnalysisRequest implements VoiceFileEventListener{
    private final VoiceFileUseCase voiceFileUseCase;
    private final SqsTemplate sqsTemplate;

    @Value("${kafka.topic.analysis.request}")
    private String queueName;


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

        try {
            sqsTemplate.send(to -> to.queue(queueName).payload(analysisRequestDto));
            log.info("Sent message via SQS: {}", analysisRequestDto);
        } catch (Exception e) {
            log.error("Failed to send message via SQS", e);
        }
    }
}
