package naeilmolae.domain.member.controller;

import jakarta.transaction.Transactional;
import naeilmolae.config.BaseTest;
import naeilmolae.domain.member.domain.Gender;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.request.WithdrawalReasonRequest;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.domain.member.dto.response.*;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
import naeilmolae.domain.weather.domain.entity.Grid;
import naeilmolae.domain.weather.domain.repository.GridRepository;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberControllerTest extends BaseTest {

    String PREFIX = "/api/v1/member";

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    YouthMemberInfoRepository youthMemberInfoRepository;
    @Autowired
    GridRepository gridRepository;


    @Test
    void 회원가입_일반_성공() {
        // given
        String username = "유성";
        MemberInfoRequestDto memberInfoRequestDto = new MemberInfoRequestDto(
                username,
                Gender.MALE,
                null,
                Role.YOUTH,
                LocalDateTime.now().minusYears(25),
                null
        );

        String uriString = UriComponentsBuilder.fromUriString(PREFIX).toUriString();

        // when
        BaseResponse<MemberIdResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(memberInfoRequestDto, getAuthHeaders(null)),
                new ParameterizedTypeReference<BaseResponse<MemberIdResponseDto>>() {
                }
        ).getBody();

        // then
        Optional<Member> byName = memberRepository.findByName(username);
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();

        assertThat(byName).isPresent();
        assertThat(byName.get().getRole()).isEqualTo(Role.YOUTH);

        List<Grid> all = gridRepository.findAll();
        for (Grid grid : all) {
            System.out.println("grid = " + grid.getId());
        }
    }

    @Test
    void 회원가입_청년_성공() {
        // given
        YouthMemberInfoDto youthMemberInfoDto = YouthMemberInfoDto.builder()
                .latitude(37.123456)
                .longitude(127.654321)
                .wakeUpTime(LocalTime.of(7, 0))
                .sleepTime(LocalTime.of(23, 0))
                .breakfast(LocalTime.of(8, 0))
                .lunch(LocalTime.of(12, 0))
                .dinner(LocalTime.of(19, 0))
                .build();

        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/youth").toUriString();

        // when
        BaseResponse<MemberIdResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                new HttpEntity<>(youthMemberInfoDto, getAuthHeaders(Role.GUEST)),
                new ParameterizedTypeReference<BaseResponse<MemberIdResponseDto>>() {
                }
        ).getBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
    }

    void 회원탈퇴_성공() {
        WithdrawalReasonRequest request = new WithdrawalReasonRequest(List.of("개인 사유", "서비스 불만"));

        String uriString = UriComponentsBuilder.fromUriString(PREFIX).toUriString();

        BaseResponse<MemberIdResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.DELETE,
                new HttpEntity<>(request, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<MemberIdResponseDto>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
    }

    @Test
    void 회원정보수정_성공() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MemberInfoRequestDto request = new MemberInfoRequestDto(
                HELPER,
                Gender.MALE,
                "https://example.com/newProfile.jpg",
                Role.HELPER,
                now,
                "newFcmToken"
        );
        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/info").toUriString();
        BaseResponse<MemberIdResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.PATCH,
                new HttpEntity<>(request, getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<MemberIdResponseDto>>() {
                }
        ).getBody();

        Member findMember = memberRepository.findByName(HELPER)
                .orElseThrow();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();

        assertThat(findMember.getProfileImage()).isEqualTo("https://example.com/newProfile.jpg");
        assertThat(findMember.getFcmToken()).isEqualTo("newFcmToken");
        assertThat(findMember.getBirth()).isEqualTo(now);
    }

    @Test
    void 청년회원정보수정_성공() {
        // given
        YouthMemberInfoUpdateDto request = YouthMemberInfoUpdateDto.builder()
                .wakeUpTime(LocalTime.of(1, 0))
                .sleepTime(LocalTime.of(2, 0))
                .breakfast(LocalTime.of(3, 0))
                .lunch(LocalTime.of(4, 0))
                .dinner(LocalTime.of(5, 0))
                .outgoingTime(LocalTime.of(6, 0))
                .build();

        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/info/youth").toUriString();
        BaseResponse<MemberIdResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.PATCH,
                new HttpEntity<>(request, getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<MemberIdResponseDto>>() {
                }
        ).getBody();
        Member findMember = memberRepository
                .findByName(YOUTH)
                .orElseThrow();
        YouthMemberInfo youthMemberInfo = findMember.getYouthMemberInfo();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
        assertThat(youthMemberInfo.getWakeUpTime().getHour()).isEqualTo(1);
        assertThat(youthMemberInfo.getSleepTime().getHour()).isEqualTo(2);
        assertThat(youthMemberInfo.getBreakfast().getHour()).isEqualTo(3);
        assertThat(youthMemberInfo.getLunch().getHour()).isEqualTo(4);
        assertThat(youthMemberInfo.getDinner().getHour()).isEqualTo(5);
        assertThat(youthMemberInfo.getOutgoingTime().getHour()).isEqualTo(6);
    }

    @Test
    void 청년회원정보조회_성공() {
        // when
        String uriString = UriComponentsBuilder
                .fromUriString(PREFIX + "/info/youth")
                .toUriString();
        BaseResponse<YouthMemberInfoResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.YOUTH)),
                new ParameterizedTypeReference<BaseResponse<YouthMemberInfoResponseDto>>() {}
        ).getBody();

        YouthMemberInfoResponseDto result = response.getResult();

        // then: 응답 및 DTO 필드 검증
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(result).isNotNull();

        // 데이터베이스에 삽입한 값과 일치하는지 검증 (예: INSERT 구문의 값)
        assertThat(result.getWakeUpTime()).isEqualTo(LocalTime.of(6, 30));
        assertThat(result.getSleepTime()).isEqualTo(LocalTime.of(23, 0));
        assertThat(result.getBreakfast()).isEqualTo(LocalTime.of(7, 30));
        assertThat(result.getLunch()).isEqualTo(LocalTime.of(12, 30));
        assertThat(result.getDinner()).isEqualTo(LocalTime.of(19, 0));
        assertThat(result.getOutgoingTime()).isEqualTo(LocalTime.of(8, 30));

        assertThat(result.isWakeUpAlarm()).isTrue();
        assertThat(result.isSleepAlarm()).isTrue();
        assertThat(result.isBreakfastAlarm()).isTrue();
        assertThat(result.isLunchAlarm()).isTrue();
        assertThat(result.isDinnerAlarm()).isTrue();
        assertThat(result.isOutgoingAlarm()).isTrue();
    }

    @Test
    void 회원정보조회_성공() {
        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX).toUriString();
        BaseResponse<MemberInfoResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.GUEST)),
                new ParameterizedTypeReference<BaseResponse<MemberInfoResponseDto>>() {
                }
        ).getBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
    }

    @Test
    void 게스트회원정보조회_성공() {
        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX).toUriString();
        BaseResponse<MemberInfoResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.GUEST)),
                new ParameterizedTypeReference<BaseResponse<MemberInfoResponseDto>>() {}
        ).getBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        MemberInfoResponseDto result = response.getResult();
        assertThat(result).isNotNull();

        assertThat(result.getNickname()).isEqualTo("게스트");
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
        assertThat(result.getProfileImage()).isEqualTo("https://example.com/profile.jpg");
        assertThat(result.getRole()).isEqualTo(Role.GUEST);
    }

    @Test
    void 봉사자_정보_조회() {
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/info/helper").toUriString();

        BaseResponse<HelperMemberInfoResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<HelperMemberInfoResponseDto>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
        System.out.println("response = " + response.getResult());
    }

    @Test
    void 조력자회원수조회_성공() {
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/helper-num").toUriString();

        BaseResponse<MemberNumResponseDto> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<MemberNumResponseDto>>() {
                }
        ).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().youthMemberNum()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void 프로필사진업로드를_위한_프리사인드URL_조회_성공() {
        String fileName = "test.jpg";
        String contentType = "image/jpeg";

        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/presigned-url")
                .queryParam("fileName", fileName)
                .queryParam("contentType", contentType)
                .toUriString();

        BaseResponse<Map<String, String>> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.GUEST)),
                new ParameterizedTypeReference<BaseResponse<Map<String, String>>>() {
                }
        ).getBody();


        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isNotNull();
        Map<String, String> result = response.getResult();
        assertThat(result).isNotNull();
        assertThat(result.get("uploadUrl")).isNotNull();
        assertThat(result.get("fileUrl")).contains("https://");
    }
}
