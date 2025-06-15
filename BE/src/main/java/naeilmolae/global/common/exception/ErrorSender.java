package naeilmolae.global.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class ErrorSender {

    private static final Logger logger = LoggerFactory.getLogger(ErrorSender.class);

    private final WebClient webClient;

    @Value("${discord.webhook.url}")
    private final String discordWebhookUrl;

    // Webhook URL은 application.yml 또는 application.properties에서 주입
    public ErrorSender(@Value("${discord.webhook.url}") String discordWebhookUrl, WebClient.Builder webClientBuilder) {
        this.discordWebhookUrl = discordWebhookUrl;
        this.webClient = webClientBuilder.build();
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Void> handleException(Exception e) {
//
//        // 에러 메시지 생성
//        String detailedMessage = buildDetailedErrorMessage(e);
//
//        logger.error("An error occurred: {}", e.getMessage(), e);
//
//        // 디스코드 알림 전송
//        sendErrorToDiscord(detailedMessage);
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
    public void sendError(Exception e) {
        String detailedMessage = buildDetailedErrorMessage(e);

        logger.error("An error occurred: {}", e.getMessage(), e);

        // 디스코드 알림 전송
        sendErrorToDiscord(detailedMessage);

    }



    private String buildDetailedErrorMessage(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw)); // 스택 트레이스를 문자열로 변환

        String stackTrace = sw.toString();
        if (stackTrace.length() > 1500) { // 메시지 크기 제한
            stackTrace = stackTrace.substring(0, 1500) + "\n... (stack trace truncated)";
        }

        return String.format(
                "**[Error Alert]**\n" +
                        "💥 *An error occurred in the application.*\n\n" +
                        "**Message:** `%s`\n" +
                        "**Exception Type:** `%s`\n" +
                        "**Stack Trace:**\n```\n%s\n```",
                escapeMarkdown(e.getMessage()),
                escapeMarkdown(e.getClass().getName()),
                escapeMarkdown(stackTrace)
        );
    }

    private String escapeMarkdown(String text) {
        return text.replaceAll("`", "\\\\`")
                .replaceAll("\\*", "\\\\*")
                .replaceAll("_", "\\\\_")
                .replaceAll("~", "\\\\~")
                .replaceAll("\\|", "\\\\|");
    }

    private void sendErrorToDiscord(String message) {
        try {
            logger.debug("Discord로 메시지 보냄: {}", message);

            // WebClient를 사용하여 POST 요청을 보내고, 응답을 무시
            webClient.post()
                    .uri(discordWebhookUrl)
                    .bodyValue(new DiscordMessage(message))
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> logger.info("Discord alert sent successfully"))
                    .doOnError(error -> logger.error("Failed to send Discord alert: {}", error.getMessage()))
                    .block();
        } catch (Exception ex) {
            logger.error("Failed to send error to Discord: {}", ex.getMessage());
        }
    }

    // 디스코드 메시지에 사용할 간단한 클래스
    private record DiscordMessage(String content) {

    }
}