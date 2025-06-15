package naeilmolae.domain.pushnotification.event;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.pushnotification.service.PushNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushNotificationScheduler {

    private final PushNotificationService pushNotificationService;

    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void schedulePushNotifications() {
        System.out.println("schedulePushNotifications 실행중~~");
        pushNotificationService.sendNotificationsAtScheduledTime();
    }

    // 매일 밤 8시에 실행
    @Scheduled(cron = "0 0 20 * * *")
    public void schedulePushNotificationsAtEight() {
        System.out.println("schedulePushNotificationsAtEight 실행중~~");
        pushNotificationService.sendNotificationsAtScheduledTimeHelper();
    }
}

