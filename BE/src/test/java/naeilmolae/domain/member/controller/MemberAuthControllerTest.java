package naeilmolae.domain.member.controller;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAuthControllerTest extends BaseTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void socialLogin() {
        String uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                .queryParam("accessToken", "123")
                .queryParam("loginType", "ANOYMOUS")
                .toUriString();

        ResponseEntity<BaseResponse<MemberLoginResponseDto>> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<BaseResponse<MemberLoginResponseDto>>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResult()).isInstanceOf(MemberLoginResponseDto.class);

        int size2 = memberRepository.findAll().size();
        System.out.println("size = " + size2);
    }

    @Test
    void regenerateToken() {
    }

    @Test
    void logout() {
    }
}