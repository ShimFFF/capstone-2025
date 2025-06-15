package naeilmolae.domain.member.service;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.*;

import java.util.List;
import java.util.Map;

public interface MemberAuthService {
    // 소셜 로그인
    MemberLoginResponseDto socialLogin(final String accessToken, LoginType loginType);
    // 새로운 액세스 토큰 발급
    MemberGenerateTokenResponseDto generateNewAccessToken(String refreshToken);
    // 로그아웃
    MemberIdResponseDto logout(Member member);


}

