package naeilmolae.domain.pushnotification.service.adapter;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.pushnotification.service.PushNotificationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationAdapterService {
    private final PushNotificationService pushNotificationService;

    public void sendNotificationThankYouMessage(Long memberId) {
        pushNotificationService.sendNotificationThankYouMessage(memberId);
    }
}