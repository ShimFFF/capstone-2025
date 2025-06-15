package naeilmolae.domain.member.strategy;

import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;

public interface LoginStrategy {
    MemberLoginResponseDto login(String accessToken);
}
