package naeilmolae.global.infrastructure.ai;

import lombok.RequiredArgsConstructor;
import naeilmolae.global.infrastructure.ai.dto.request.ChatGptRequest;
import naeilmolae.global.infrastructure.ai.dto.response.ChatGptResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiApiClient {

    private final WebClient webClient;

    public ChatGptResponse sendRequestToModel(String model, List<ChatGptRequest.ChatGptMessage> messages, int maxTokens, double temperature) {
        ChatGptRequest request = new ChatGptRequest(model, messages, maxTokens, temperature);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGptResponse.class)
                .block();
    }
}

