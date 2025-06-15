package naeilmolae.domain.voicefile.controller;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.voicefile.application.dto.request.ReportRequestDto;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileReportRepository;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileRepository;
import naeilmolae.domain.voicefile.application.usecase.ProvidedFileUseCase;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

class ReportControllerTest extends BaseTest {

    final String PREFIX = "/api/v1/providedfile";
    @Autowired
    ProvidedFileRepository providedFileRepository;

    @Autowired
    ProvidedFileUseCase providedFileUseCase;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProvidedFileReportRepository providedFileReportRepository;

    @Test
    void 봉사자_감사_메시지_신고_성공() {
        // 이전 조회
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/list")
                .queryParam("parentCategory", AlarmCategory.GO_OUT)
                .toUriString();
        String prev_response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<String>() {}
        ).getBody();
        System.out.println("response = " + prev_response);

        // 요청
        Long providedFileId = 4L;
        String reportUriString = UriComponentsBuilder.fromUriString(PREFIX + "/{providedFileId}/report")
                .buildAndExpand(providedFileId)
                .toUriString();
        ReportRequestDto reason = new ReportRequestDto("reason");
        BaseResponse<Boolean> body = restTemplate.exchange(
                reportUriString,
                HttpMethod.POST,
                new HttpEntity<>(reason, getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<Boolean>>() {
                }
        ).getBody();
        System.out.println("body = " + body.getResult());

        // 이후 조회
        String uriString2 = UriComponentsBuilder.fromUriString(PREFIX + "/list")
                .queryParam("parentCategory", AlarmCategory.GO_OUT)
                .toUriString();
        String after_response = restTemplate.exchange(
                uriString2,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<String>() {}
        ).getBody();
        System.out.println("response = " + after_response);

//
//        List<ProvidedFileReport> all =
//                providedFileReportRepository.findAll();
//
//        for (ProvidedFileReport providedFileReport : all) {
//            System.out.println("providedFileReport = " + providedFileReport.getProvidedFile().getId());
//        }

    }

}