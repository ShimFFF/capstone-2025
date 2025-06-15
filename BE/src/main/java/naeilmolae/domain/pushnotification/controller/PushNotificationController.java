package naeilmolae.domain.pushnotification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.global.common.base.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "푸시 알림 테스트 API", description = "푸시 알림 관련 API")
@RestController
@RequestMapping("/api/v1/test-push")
@RequiredArgsConstructor
@Slf4j
public class PushNotificationController {

    private final FirebaseMessagingService firebaseMessagingService;

    //1. fcmToken, title, alarmId 로 테스트하는 API (청년 알림)
    @PostMapping("/alarm")
    @Operation(summary = "청년 푸시 알림 테스트 API", description = "fcmToken, title, alarmId 로 테스트하는 API (청년 알림)")
    public BaseResponse<String> sendAlarmNotification(
            @RequestParam String fcmToken,
            @RequestParam String title,
            @RequestParam Long alarmId
    ) {
        firebaseMessagingService.sendNotification(fcmToken, title, alarmId);
        return BaseResponse.onSuccess("Alarm notification sent!");
    }

    // 2. fcmToken, NotificationType 으로 테스트하는 API (봉사자 알림)
    @PostMapping("/event")
    @Operation(summary = "봉사자 푸시 알림 테스트 API", description = "fcmToken, NotificationType 으로 테스트하는 API (봉사자 알림)")
    public BaseResponse<String> sendEventNotification(
            @RequestParam String fcmToken,
            @RequestParam NotificationType notificationType
    ) {
        firebaseMessagingService.sendNotification(fcmToken, notificationType);
        return BaseResponse.onSuccess("Alarm notification sent!");
    }
}
