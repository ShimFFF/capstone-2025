package naeilmolae.domain.voicefile.service;

import naeilmolae.domain.voicefile.application.usecase.VoiceFileUseCase;
import naeilmolae.global.common.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class VoiceFileUseCaseTest {

    @Autowired
    private VoiceFileUseCase voiceFileUseCase;

    @Test
    void verifyUserFile_success() {
        voiceFileUseCase.verifyContent("일어날 때가 된 청년에게,\n" +
                "아침을 깨우는 한 마디.", "아침이야. 일어나서 간단한 스트레칭을 하고 아침의 피로를 날려보자.");

    }
    @Test
    void verifyUserFile_fail_201() {
        try {
            voiceFileUseCase.verifyContent("잠자기 전에 ", "안녕하세요. 오늘도 좋은 하루 되세요.");
        } catch (RestApiException e) {
            assertEquals("ANALYSIS200", e.getErrorCode().getCode());
        }
    }
    @Test
    void verifyUserFile_fail_202() {
        try {
            voiceFileUseCase.verifyContent("잠자기 전에 ", "죽어버려 정신나간 녀석. 너는 사회의 악이야.");
        } catch (RestApiException e) {
            assertEquals("ANALYSIS201", e.getErrorCode().getCode());
        }
    }
}