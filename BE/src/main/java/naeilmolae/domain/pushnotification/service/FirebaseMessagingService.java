package naeilmolae.domain.pushnotification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseMessagingService.class);

    public void sendNotification(String fcmToken, String content, Long alarmId) {
        // FirebaseMessage 생성
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("내일모래")
                        .setBody(content)
                        .build())
                .putData("alarmId", String.valueOf(alarmId)) // 파일 ID를 데이터로 추가
                .setApnsConfig( // iOS 푸시 알림 설정
                        ApnsConfig.builder()
                                .putHeader("apns-priority", "10") // 푸시 알림 우선순위 설정(즉시)
                                .setAps(
                                        Aps.builder()
                                                .setAlert(
                                                        ApsAlert.builder()
                                                                .setTitle("내일모래")
                                                                .setBody(content)
                                                                .build()
                                                )
                                                .setSound("default")
                                                .setContentAvailable(true)
                                                .build()
                                )
                                .build()
                )
                .build();
        sendNotification(message);
    }

    // 이벤트가 발생하면 알림을 보내는 메서드, 제목과 콘텐트와 fcm 토큰을 받고 이 서비스로 알람을 보내줌
    public void sendNotification(String fcmToken, NotificationType notificationType) {
        // FirebaseMessage 생성
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(notificationType.getTitle())
                        .setBody(notificationType.getContent())
                        .build())
                .putData("notificationType", notificationType.name()) // 파일 ID를 데이터로 추가
                .setApnsConfig( // iOS 푸시 알림 설정
                        ApnsConfig.builder()
                                .putHeader("apns-priority", "10") // 푸시 알림 우선순위 설정(즉시)
                                .setAps(
                                        Aps.builder()
                                                .setAlert(
                                                        ApsAlert.builder()
                                                                .setTitle(notificationType.getTitle())
                                                                .setBody(notificationType.getContent())
                                                                .build()
                                                )
                                                .setSound("default")
                                                .setContentAvailable(true)
                                                .build()
                                )
                                .build()
                )
                .build();
        sendNotification(message);
    }

    // FCM에 메시지 전송
    public void sendNotification(Message message) {
        try {
            // FCM에 메시지 전송
            String response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("ios")).send(message);
            logger.info("Successfully sent message: {}", response);
            logger.info("Notification details - message: {}", message.toString());
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage());
            logger.error("Failed to send notification to message: {}", message.toString());
        }
    }
}
