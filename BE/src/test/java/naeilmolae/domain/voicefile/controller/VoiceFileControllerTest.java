package naeilmolae.domain.voicefile.controller;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.application.dto.request.UploadContentRequestDto;
import naeilmolae.domain.voicefile.application.dto.response.AvailableVoiceFileResponseDto;
import naeilmolae.domain.voicefile.application.dto.response.RetentionDto;
import naeilmolae.domain.voicefile.application.dto.response.VoiceFileMetaResponseDto;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileRepository;
import naeilmolae.domain.voicefile.domain.repository.VoiceFileRepository;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VoiceFileControllerTest extends BaseTest {
    @Autowired
    VoiceFileRepository voiceFileRepository;
    @Autowired
    ProvidedFileRepository providedFileRepository;

    String PREFIX = "/api/v1/voicefiles";

    @Test
    void 봉사자_탈퇴전_retention_조회_성공() {
        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/retention").toUriString();
        BaseResponse<RetentionDto> body = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<RetentionDto>>() {
                }
        ).getBody();
        RetentionDto result = body.getResult();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getVoiceCount()).isEqualTo(6);
        assertThat(result.getThanksCount()).isEqualTo(2);
        assertThat(result.getMessageCount()).isEqualTo(2);
    }

    @Test
    void 봉사자_스크립트_gpt_성공() {
        // when
        Long alarmId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{alarmId}/self")
                .buildAndExpand(alarmId)
                .toUriString();
        String content = "잘 잤어? 좋은 아침이야.";
        UploadContentRequestDto requestBody = new UploadContentRequestDto(content);
        BaseResponse<VoiceFileMetaResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<VoiceFileMetaResponseDto>>() {
                }
        ).getBody();
        VoiceFileMetaResponseDto result = response.getResult();

        VoiceFile voiceFile = voiceFileRepository.findById(result.getVoiceFileId())
                .orElseThrow();
        // then
        assertThat(voiceFile.getContent()).isEqualTo(content);
        assertThat(voiceFile.getAlarmId()).isEqualTo(alarmId);
        assertThat(voiceFile.getAnalysisResult()).isNull();
    }

    @Test
    void 봉사자_스크립트_gpt_실패_올바르지_않은_내용() {
        // when
        Long alarmId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{alarmId}/self")
                .buildAndExpand(alarmId)
                .toUriString();
        String content = "파스타 맛있더라";
        UploadContentRequestDto requestBody = new UploadContentRequestDto(content);
        ErrorResponse response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<ErrorResponse>() {
                }
        ).getBody();
        System.out.println("response = " + response);
//         then
        assertThat(response).isNotNull();
        assertThat(response.code).isEqualTo("ANALYSIS200");
    }

    @Test
    void 봉사자_스크립트_gpt_실패_비속어() {
        // when
        Long alarmId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{alarmId}/self")
                .buildAndExpand(alarmId)
                .toUriString();
        String content = "잘 잤어? 좋은 아침이야. ㅅㅂ";
        UploadContentRequestDto requestBody = new UploadContentRequestDto(content);
        ErrorResponse response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<ErrorResponse>() {
                }
        ).getBody();
        // then
        assertThat(response).isNotNull();
        assertThat(response.code).isEqualTo("ANALYSIS201");
    }

    @Test
    void 봉사자_분석_요청_결과_아직_분석_중이지_않음() {
        // given
        Long voiceFileId = 4L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/analysis/{voiceFileId}")
                .buildAndExpand(voiceFileId)
                .toUriString();

        ErrorResponse response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<ErrorResponse>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.code).isEqualTo("ANALYSIS001");
    }

    @Test
    void 봉사자_분석_요청_결과_그대로_읽지_않음() {
        // given
        Long voiceFileId = 5L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/analysis/{voiceFileId}")
                .buildAndExpand(voiceFileId)
                .toUriString();

        ErrorResponse response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<ErrorResponse>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.code).isEqualTo("ANALYSIS003");
    }

    @Test
    void 봉사자_분석_요청_결과_욕설() {
        // given
        Long voiceFileId = 6L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/analysis/{voiceFileId}")
                .buildAndExpand(voiceFileId)
                .toUriString();

        ErrorResponse response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<ErrorResponse>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.code).isEqualTo("ANALYSIS002");
    }

    @Test
    void 청년_응원_메시지_조회_성공() {
        // given
        Long alarmId = 6L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX)
                .queryParam("alarm-id", alarmId)
                .toUriString();

        BaseResponse<AvailableVoiceFileResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.GUEST)),
                new ParameterizedTypeReference<BaseResponse<AvailableVoiceFileResponseDto>>() {
                }
        ).getBody();


        // then
        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        System.out.println("response = " + response.getResult());
    }

    static class ErrorResponse {
        LocalDateTime timestamp;
        String code;
        String message;

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ErrorResponse{" +
                    "timestamp=" + timestamp +
                    ", code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}