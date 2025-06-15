package naeilmolae.domain.pushnotification.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.application.usecase.AlarmUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class SleepNotificationStrategy implements NotificationStrategy {

    private final MessageSource messageSource;

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getSleepTime() != null && info.isSleepAlarm() &&
                now.getHour() == info.getSleepTime().getHour() &&
                now.getMinute() == info.getSleepTime().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmUseCase alarmUsecase) {
        String message = messageSource.getMessage(
                "notification.sleep.message",
                null,
                Locale.getDefault()
        );

        log.info("Sending sleep notification to member: {}, name: {}", member.getId(), member.getName());

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmUsecase.findByAlarmCategory(AlarmCategory.MEAL_DINNER).getAlarmId()
        );
    }
}