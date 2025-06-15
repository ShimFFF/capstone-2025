package naeilmolae.domain.member.mapper;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.global.config.security.jwt.TokenInfo;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public static Member toMember(final String clientId, LoginType loginType){
        return Member.builder()
                .clientId(clientId)
                .loginType(loginType)
                .build();
    }

    public static MemberLoginResponseDto toMemberLoginResponseDto(final Member member, TokenInfo tokenInfo, boolean isServiceMember, Role role) {
        return MemberLoginResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getName())
                .accessToken(tokenInfo.accessToken())
                .refreshToken(tokenInfo.refreshToken())
                .isServiceMember(isServiceMember)
                .role(role)
                .infoRegistered(member.getGender() != null && member.getBirth() != null)
                .locationRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getLatitude() != null && member.getYouthMemberInfo().getLongitude() != null)
                .pushTimeRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getWakeUpTime() != null && member.getYouthMemberInfo().getSleepTime() != null && member.getYouthMemberInfo().getBreakfast() != null && member.getYouthMemberInfo().getLunch() != null && member.getYouthMemberInfo().getDinner() != null)
                .build();
    }

    public static YouthMemberInfo toYouthMemberInfo(YouthMemberInfoDto dto) {
        if (dto == null) {
            return YouthMemberInfo.builder()
                    .wakeUpTime(DefaultTimeSettings.DEFAULT_WAKE_UP_TIME)
                    .sleepTime(DefaultTimeSettings.DEFAULT_SLEEP_TIME)
                    .breakfast(DefaultTimeSettings.DEFAULT_BREAKFAST_TIME)
                    .lunch(DefaultTimeSettings.DEFAULT_LUNCH_TIME)
                    .dinner(DefaultTimeSettings.DEFAULT_DINNER_TIME)
                    .build();
        }

        return YouthMemberInfo.builder()
                .wakeUpTime(dto.getWakeUpTime() != null ? dto.getWakeUpTime() : DefaultTimeSettings.DEFAULT_WAKE_UP_TIME)
                .sleepTime(dto.getSleepTime() != null ? dto.getSleepTime() : DefaultTimeSettings.DEFAULT_SLEEP_TIME)
                .breakfast(dto.getBreakfast() != null ? dto.getBreakfast() : DefaultTimeSettings.DEFAULT_BREAKFAST_TIME)
                .lunch(dto.getLunch() != null ? dto.getLunch() : DefaultTimeSettings.DEFAULT_LUNCH_TIME)
                .dinner(dto.getDinner() != null ? dto.getDinner() : DefaultTimeSettings.DEFAULT_DINNER_TIME)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    public static YouthMemberInfoDto toYouthMemberInfoDto(YouthMemberInfo youthMemberInfo) {
        return YouthMemberInfoDto.builder()
                .wakeUpTime(youthMemberInfo.getWakeUpTime() != null ? youthMemberInfo.getWakeUpTime() : null)
                .sleepTime(youthMemberInfo.getSleepTime() != null ? youthMemberInfo.getSleepTime() : null)
                .breakfast(youthMemberInfo.getBreakfast() != null ? youthMemberInfo.getBreakfast() : null)
                .lunch(youthMemberInfo.getLunch() != null ? youthMemberInfo.getLunch() : null)
                .dinner(youthMemberInfo.getDinner() != null ? youthMemberInfo.getDinner() : null)
                .latitude(youthMemberInfo.getLatitude() != null ? youthMemberInfo.getLatitude() : null)
                .longitude(youthMemberInfo.getLongitude() != null ? youthMemberInfo.getLongitude() : null)
                .build();
    }


    public static MemberInfoResponseDto toMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .role(member.getRole())
                .birth(member.getBirth())
                .nickname(member.getName())
                .gender(member.getGender())
                .profileImage(member.getProfileImage())
                .infoRegistered(member.getGender() != null && member.getBirth() != null)
                .locationRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getLatitude() != null && member.getYouthMemberInfo().getLongitude() != null)
                .pushTimeRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getWakeUpTime() != null && member.getYouthMemberInfo().getSleepTime() != null && member.getYouthMemberInfo().getBreakfast() != null && member.getYouthMemberInfo().getLunch() != null && member.getYouthMemberInfo().getDinner() != null)
                .build();
    }

    public static MemberInfoResponseDto toMemberInfoResponseDto(Member member, YouthMemberInfoDto youthMemberInfoDto) {
        return MemberInfoResponseDto.builder()
                .role(member.getRole())
                .birth(member.getBirth())
                .nickname(member.getName())
                .gender(member.getGender())
                .profileImage(member.getProfileImage())
                .infoRegistered(member.getGender() != null && member.getBirth() != null)
                .locationRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getLatitude() != null && member.getYouthMemberInfo().getLongitude() != null)
                .pushTimeRegistered(member.getYouthMemberInfo() != null && member.getYouthMemberInfo().getWakeUpTime() != null && member.getYouthMemberInfo().getSleepTime() != null && member.getYouthMemberInfo().getBreakfast() != null && member.getYouthMemberInfo().getLunch() != null && member.getYouthMemberInfo().getDinner() != null)
                .youthMemberInfoDto(youthMemberInfoDto)
                .build();
    }
}
