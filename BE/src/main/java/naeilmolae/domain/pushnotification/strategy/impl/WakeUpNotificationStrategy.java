package naeilmolae.domain.pushnotification.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.alarm.application.usecase.AlarmUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

import static naeilmolae.domain.alarm.domain.entity.AlarmCategory.WAKE_UP_WEEKDAY;

@Component
@RequiredArgsConstructor
@Slf4j
public class WakeUpNotificationStrategy implements NotificationStrategy {

    private final MessageSource messageSource;

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getWakeUpTime() != null && info.isWakeUpAlarm() &&
                now.getHour() == info.getWakeUpTime().getHour() &&
                now.getMinute() == info.getWakeUpTime().getMinute();
    }

    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmUseCase alarmUsecase) {
        String message = messageSource.getMessage(
                "notification.wakeup.message",
                null,
                Locale.getDefault()
        );

        log.info("Sending wakeup notification to member: {}, name: {}", member.getId(), member.getName());

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmUsecase.findByAlarmCategory(WAKE_UP_WEEKDAY).getAlarmId()
        );
    }
}
