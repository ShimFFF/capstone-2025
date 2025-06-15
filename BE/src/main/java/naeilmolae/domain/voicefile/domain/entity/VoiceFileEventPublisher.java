package naeilmolae.domain.voicefile.domain.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoiceFileEventPublisher {
    private static ApplicationEventPublisher applicationEventPublisher;


    public static void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
