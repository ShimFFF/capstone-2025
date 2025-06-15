package naeilmolae.domain.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AnalysisErrorStatus;
import naeilmolae.global.infrastructure.ai.OpenAiApiClient;
import naeilmolae.global.infrastructure.ai.dto.request.ChatGptRequest;
import naeilmolae.global.infrastructure.ai.dto.response.ChatGptResponse;
import naeilmolae.global.templates.PromptManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public final class ChatGptService {

    private final OpenAiApiClient openAiApiClient;
    private final PromptManager promptManager;

    private final String model = "gpt-4o";

    private final String systemRole = "system";

    private final String userRole = "user";
    private final String userPrompt = "Is this sentence offensive: ";

    private final int maxTokens = 100;
    private final double temperature = 0.5;

    private final ObjectMapper objectMapper = new ObjectMapper();



    public ScriptValidationResponseDto getCheckScriptRelevancePrompt(String situation, String sentence) {
        // 템플릿 생성
        String prompt = promptManager.createCheckForOffensiveLanguagePrompt(situation, sentence);
        log.info("script validation script : {}", prompt);

        ChatGptResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new ChatGptRequest.ChatGptMessage(systemRole, prompt)
                ),
                maxTokens,
                temperature
        );

        String content = response.getChoices().get(0).getMessage().getContent().trim().toLowerCase();
        System.out.println(content);
        try {
            ScriptValidationResponseDto scriptValidationResponseDto = objectMapper.readValue(content, ScriptValidationResponseDto.class);

            if (scriptValidationResponseDto.getReason() == null) {
                return scriptValidationResponseDto;
            }

            switch(scriptValidationResponseDto.getReason()) {
                case 0:
                    throw new RestApiException(AnalysisErrorStatus._INVALID_CONTEXT);
                case 1:
                    throw new RestApiException(AnalysisErrorStatus._PROFANITY_DETECTED);
                default:
                    throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
            }

        } catch (JsonMappingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        } catch (JsonProcessingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        }
    }
    public ScriptValidationResponseDto getCheckScriptRelevancePrompt2(String stat1, String stat2) {
        String prompt = promptManager.createCheckForOffensiveLanguagePrompt2(stat1, stat2);

        ChatGptResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new ChatGptRequest.ChatGptMessage(systemRole, prompt)
                ),
                maxTokens,
                temperature
        );

        log.info("gpt response : {}", response);

        String content = response.getChoices().get(0).getMessage().getContent().trim().toLowerCase();
        System.out.println(content);
        try {
            return  objectMapper.readValue(content, ScriptValidationResponseDto.class);
        } catch (JsonMappingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        } catch (JsonProcessingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        }
    }
}

