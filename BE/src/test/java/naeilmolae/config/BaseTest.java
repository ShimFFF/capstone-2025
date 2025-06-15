package naeilmolae.config;

import naeilmolae.domain.member.domain.Gender;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.PrincipalDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll에서 static 없이 사용할 수 있도록 설정
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BaseTest {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    MemberRepository memberRepository;

    protected String YOUTH = "청년";
    protected String HELPER = "김희";

    @BeforeAll
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    protected HttpHeaders getAuthHeaders(Role role) {
        String uriString = null;

        switch (role) {
            case Role.YOUTH:
                uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                        .queryParam("accessToken", "client-id-4")
                        .queryParam("loginType", "ANOYMOUS")
                        .toUriString();
                break;
            case Role.HELPER:
                uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                        .queryParam("accessToken", "client-id-2")
                        .queryParam("loginType", "ANOYMOUS")
                        .toUriString();
                break;
            case Role.GUEST:
                uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                        .queryParam("accessToken", "client-id-5")
                        .queryParam("loginType", "ANOYMOUS")
                        .toUriString();
                break;
            case null:
                uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                        .queryParam("accessToken", "123")
                        .queryParam("loginType", "ANOYMOUS")
                        .toUriString();
                break;
            default:
                throw new RuntimeException("Role이 잘못되었습니다.");
        }

        ResponseEntity<BaseResponse<MemberLoginResponseDto>> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<BaseResponse<MemberLoginResponseDto>>() {}
        );
        MemberLoginResponseDto result = response.getBody().getResult();
        String accessToken = result.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
        return headers;
    }

}