package naeilmolae.domain.pushnotification.strategy.impl;

import com.google.api.client.util.ArrayMap;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutingNotificationStrategy  implements NotificationStrategy {

    private final MessageSource messageSource;

    private Map<Long, AlarmCategory> alarmCategoryMap = new ArrayMap<>();


    public boolean updateAlarmCategoryMap(Map<Long, AlarmCategory> result) {
        alarmCategoryMap = result;

        return true;
    }

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.isOutgoingAlarm() &&
                now.getHour() == info.getOutgoingTime().getHour() &&
                now.getMinute() == info.getOutgoingTime().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmUseCase alarmUsecase) {
        String message = messageSource.getMessage(
                "notification.outing.message",
                null,
                Locale.getDefault()
        );

        log.info("Sending outgoing notification to member: {}, name: {}", member.getId(), member.getName());

        // TEST
        AlarmCategory category = alarmCategoryMap.get(member.getId());
        if(category == null) {
            System.out.println("category is null");
            category = AlarmCategory.GO_OUT_CLEAR;
        }

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmUsecase.findByAlarmCategory(AlarmCategory.GO_OUT_CLEAR).getAlarmId()
        );
    }
}