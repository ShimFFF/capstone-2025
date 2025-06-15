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

import static naeilmolae.domain.alarm.domain.entity.AlarmCategory.MEAL_BREAKFAST;

@Component
@RequiredArgsConstructor
@Slf4j
public class BreakfastNotificationStrategy implements NotificationStrategy {

    private final MessageSource messageSource;

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getBreakfast() != null && info.isBreakfastAlarm() &&
                now.getHour() == info.getBreakfast().getHour() &&
                now.getMinute() == info.getBreakfast().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmUseCase alarmUsecase) {
        String message = messageSource.getMessage(
                "notification.meal.message",
                null,
                Locale.getDefault()
        );

        log.info("Sending breakfast notification to member: {}, name: {}", member.getId(), member.getName());

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmUsecase.findByAlarmCategory(MEAL_BREAKFAST).getAlarmId()
        );
    }
}
