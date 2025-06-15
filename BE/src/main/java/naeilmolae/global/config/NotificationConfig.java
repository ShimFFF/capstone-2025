package naeilmolae.global.config;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import naeilmolae.domain.pushnotification.strategy.impl.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class NotificationConfig {

    private final MessageSource messageSource;

    @Bean
    public List<NotificationStrategy> notificationStrategies() {
        return List.of(
                new WakeUpNotificationStrategy(messageSource),
                new BreakfastNotificationStrategy(messageSource),
                new LunchNotificationStrategy(messageSource),
                new DinnerNotificationStrategy(messageSource),
                new SleepNotificationStrategy(messageSource),
                new OutingNotificationStrategy(messageSource)
        );
    }
}
