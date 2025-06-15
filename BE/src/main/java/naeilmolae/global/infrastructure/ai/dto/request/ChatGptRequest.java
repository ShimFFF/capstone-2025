package naeilmolae.global.infrastructure.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<ChatGptMessage> messages;
    private int max_tokens;
    private double temperature;
    @Data
    @AllArgsConstructor
    public static class ChatGptMessage {
        private String role; // "system", "user", "assistant"
        private String content;
    }
}
