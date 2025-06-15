package naeilmolae.domain.pushnotification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
    WELCOME_REMINDER("청년들이 당신의 목소리를 기다려요!", "지금 눌러서 녹음하기"),
    THANK_YOU_MESSAGE("청년들로부터 감사 편지가 도착했어요!", "지금 눌러서 확인하기");

    private final String title;
    private final String content;

    NotificationType(String title, String content) {
        this.title = title;
        this.content = content;
    }

}