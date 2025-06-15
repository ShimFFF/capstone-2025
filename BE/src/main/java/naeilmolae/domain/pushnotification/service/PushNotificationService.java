package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.usecase.AlarmUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.domain.pushnotification.strategy.context.NotificationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final MemberAdapterService memberAdapterService;
    private final FirebaseMessagingService firebaseMessagingService; // Firebase 연동
    private final AlarmUseCase alarmUsecase;
    private final NotificationContext notificationContext;

    // 매일 정해진 시간에 알림을 보내는 메서드
    @Transactional(readOnly = true)
    public void sendNotificationsAtScheduledTime() {
        List<Member> youthMembers = memberAdapterService.getAllYouthMember();
        LocalDateTime now = LocalDateTime.now();

        // 알림 시간에 따라 알림 전략 실행
        for (Member member : youthMembers) {
            if (member.getFcmToken() != null && !member.getFcmToken().isEmpty()) {
                notificationContext.executeStrategies(member, firebaseMessagingService, alarmUsecase, now);
            }
        }
    }

    // 매일 밤 8시에 실행, 활동 3일 이상 경과한 경우 알림 전송
    public void sendNotificationsAtScheduledTimeHelper() {
        List<Member> helperMembers = memberAdapterService.getAllHelperMember();

        LocalDateTime now = LocalDateTime.now();
        for (Member member : helperMembers) {
            LocalDateTime lastLoginDate = memberAdapterService.getLastLoginDate(member);

            // 마지막 활동일이 null이 아니고, 3일 이상 경과한 경우 알림 전송
            if (lastLoginDate != null && lastLoginDate.isBefore(now.minusDays(3)) && member.getHelperMemberInfo().isWelcomeReminder()) {
                firebaseMessagingService.sendNotification(member.getFcmToken(), NotificationType.WELCOME_REMINDER);
            }
        }
    }

    @Transactional(readOnly = true)
    public void sendNotificationThankYouMessage(Long memberId) {
        Member member = memberAdapterService.findById(memberId);
        if (member == null || member.getHelperMemberInfo() == null) {
            return;
        }

        if (member.getHelperMemberInfo().isThankYouMessage()) {
            firebaseMessagingService.sendNotification(member.getFcmToken(), NotificationType.THANK_YOU_MESSAGE);
        }
    }
}
