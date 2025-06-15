package naeilmolae.domain.chatgpt.service;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AnalysisErrorStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ChatGptServiceTest extends BaseTest {

    @Autowired
    ChatGptService chatGptService;

    @Test
    void 음성_분석_성공() {
        String stat1 = "안녕. 좋은 하루보내!";
        String stat2 = "안녕 존 하루 보내";
        ScriptValidationResponseDto response = chatGptService.getCheckScriptRelevancePrompt2(stat1, stat2);

        assertThat(response.isProper()).isTrue();
    }

    @Test
    void 음성_분석_실패_추가_문장() {
        String stat1 = "안녕. 좋은 하루보내!";
        String stat2 = "안녕 존 하루 보내! 파이팅!";
        ScriptValidationResponseDto response = chatGptService.getCheckScriptRelevancePrompt2(stat1, stat2);

        assertThat(response.isProper()).isFalse();
        assertThat(response.getReason()).isEqualTo(0);
    }

    @Test
    void 음성_분석_실패_거북한_표현() {
        String stat1 = "안녕. 좋은 하루보내!";
        String stat2 = "안녕 존 하루 보내! 불쌍해라!";
        ScriptValidationResponseDto response = chatGptService.getCheckScriptRelevancePrompt2(stat1, stat2);

        assertThat(response.isProper()).isFalse();
        assertThat(response.getReason()).isEqualTo(1);
    }

    @Test
    void 텍스트_분석_성공() {
        String situation = "일어날 때가 된 청년에게,\\n아침을 깨우는 한 마디.";
        String statement = "좋은 아침이야! 행복한 하루 보내";
        ScriptValidationResponseDto response = chatGptService.getCheckScriptRelevancePrompt(situation, statement);

        assertThat(response.isProper()).isTrue();
    }

    @Test
    void 텍스트_분석_실패_상황에_맞지_않은_응원() {
        String situation = "일어날 때가 된 청년에게,\\n아침을 깨우는 한 마디.";
        String statement = "좋은 하루 보내! 나 오늘 시험인데 기도해줘";

        assertThatThrownBy(() -> chatGptService.getCheckScriptRelevancePrompt(situation, statement))
                .isInstanceOf(RestApiException.class)
                .satisfies(e -> {
                    RestApiException restApiException = (RestApiException) e;
                    assertThat(restApiException.getErrorCode().getCode()).isEqualTo("ANALYSIS200");
                });
    }

    @Test
    void 텍스트_분석_실패_상황에_불쾌한_표현() {
        String situation = "일어날 때가 된 청년에게,\\n아침을 깨우는 한 마디.";
        String statement = "좋은 아침이야! 행복한 하루 보내. 열심히라도 살아야지. 힘내라";

        assertThatThrownBy(() -> chatGptService.getCheckScriptRelevancePrompt(situation, statement))
                .isInstanceOf(RestApiException.class)
                .satisfies(e -> {
                    RestApiException restApiException = (RestApiException) e;
                    assertThat(restApiException.getErrorCode().getCode()).isEqualTo("ANALYSIS200");
                });
    }
}