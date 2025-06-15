package naeilmolae.domain.member.dto.request;

import naeilmolae.domain.member.domain.Gender;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;

import java.time.LocalDateTime;

public record MemberInfoRequestDto(
        String name,
        Gender gender,
        String profileImage,
        Role role,
        LocalDateTime birth,
        String fcmToken
){
}
