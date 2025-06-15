package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.*;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.strategy.context.LoginContext;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import naeilmolae.global.config.security.jwt.JwtProvider;
import naeilmolae.global.config.security.jwt.TokenInfo;
import naeilmolae.global.config.security.jwt.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAuthServiceImpl implements MemberAuthService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenService refreshTokenService;
    private final JwtProvider jwtTokenProvider;
    private final LoginContext loginContext;

    // 소셜 로그인을 수행하는 함수
    @Override
    @Transactional
    public MemberLoginResponseDto socialLogin(String accessToken, LoginType loginType) {
        return loginContext.executeStrategy(accessToken, loginType);
    }

    // 새로운 액세스 토큰 발급 함수
    @Override
    @Transactional
    public MemberGenerateTokenResponseDto generateNewAccessToken(String refreshToken) {

        // 만료된 refreshToken인지 확인
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw new RestApiException(AuthErrorStatus.EXPIRED_REFRESH_TOKEN);

        //편의상 refreshToken으로 회원을 조회하는 방식으로 감 (무조건 고쳐야 함)
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN));

        // 새로운 토큰
        String newAccessToken = jwtTokenProvider.generateToken(
                member.getId().toString(), member.getRole().toString(), TokenType.ACCESS);
        String newRefreshToken = jwtTokenProvider.generateToken(
                member.getId().toString(), member.getRole().toString(), TokenType.REFRESH);

        // 새로운 토큰 저장
        refreshTokenService.saveRefreshToken(newRefreshToken, member);

        return new MemberGenerateTokenResponseDto(
                newRefreshToken, newAccessToken
        );
    }

    // 로그아웃 함수
    @Override
    @Transactional
    public MemberIdResponseDto logout(Member member) {
        Member loginMember = memberService.findById(member.getId());

        refreshTokenService.deleteRefreshToken(loginMember);
        return new MemberIdResponseDto(loginMember.getId());
    }

}