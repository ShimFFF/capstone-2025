package naeilmolae.domain.voicefile.controller;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.pushnotification.service.adapter.PushNotificationAdapterService;
import naeilmolae.domain.voicefile.application.dto.request.ThanksMessageRequestDto;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileRepository;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;

class ProvidedFileControllerTest extends BaseTest {

    final String PREFIX = "/api/v1/providedfile";
    @Autowired
    ProvidedFileRepository providedFileRepository;

    @MockBean
    private PushNotificationAdapterService pushNotificationAdapterService;

    @Test
    void 봉사자_감사_메시지_조회_성공() {
        // given
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/list")
                .queryParam("parentCategory", AlarmCategory.WAKE_UP)
                .toUriString();

        String response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<String>() {
                }
        ).getBody();

        System.out.println("response = " + response);
    }

    @Test
    void 청년_감사_메시지_보내기_성공() {
        doNothing().when(pushNotificationAdapterService).sendNotificationThankYouMessage(anyLong());

        Long providedFileId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/comment")
                .buildAndExpand(providedFileId)
                .toUriString();

        ThanksMessageRequestDto requestBody = new ThanksMessageRequestDto("감사합니다람쥐");

        BaseResponse<List<String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<List<String>>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().size()).isEqualTo(3);
    }

    @Test
    void 청년_감사_메시지_성공_중복메시지() {
        doNothing().when(pushNotificationAdapterService).sendNotificationThankYouMessage(anyLong());

        Long providedFileId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/comment")
                .buildAndExpand(providedFileId)
                .toUriString();

        ThanksMessageRequestDto requestBody = new ThanksMessageRequestDto("사랑해요");

        BaseResponse<List<String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<List<String>>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().size()).isEqualTo(2);
    }

    @Test
    void 청년_감사_메시지_보내기_실패() {
        doNothing().when(pushNotificationAdapterService).sendNotificationThankYouMessage(anyLong());

        Long providedFileId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/comment")
                .buildAndExpand(providedFileId)
                .toUriString();

        ThanksMessageRequestDto requestBody = new ThanksMessageRequestDto("감사합니다람쥐");

        // 404 예외가 발생하는 것을 검증
        BaseResponse<List<String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.GUEST)), // 다른 id
                new ParameterizedTypeReference<BaseResponse<List<String>>>() {
                }
        ).getBody();
    }

    @Test
    void 청년_감사_메시지_삭제_성공() {
        doNothing().when(pushNotificationAdapterService).sendNotificationThankYouMessage(anyLong());

        Long providedFileId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/comment")
                .buildAndExpand(providedFileId)
                .toUriString();

        ThanksMessageRequestDto requestBody = new ThanksMessageRequestDto("사랑해요");

        BaseResponse<List<String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.DELETE,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<List<String>>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().size()).isEqualTo(1);
    }

    @Test
    void 청년_감사_메시지_삭제_없는_메시지_성공() {
        doNothing().when(pushNotificationAdapterService).sendNotificationThankYouMessage(anyLong());

        Long providedFileId = 1L;
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/comment")
                .buildAndExpand(providedFileId)
                .toUriString();

        ThanksMessageRequestDto requestBody = new ThanksMessageRequestDto("ㄹㅇㄴㅁㄹㅇㅁ");

        BaseResponse<List<String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.DELETE,
                new HttpEntity<>(requestBody, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<List<String>>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().size()).isEqualTo(2);
    }

}