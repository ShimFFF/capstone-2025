package naeilmolae.domain.member.strategy.impl;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.client.KakaoMemberClient;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.service.MemberRefreshTokenService;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.member.strategy.LoginStrategy;
import naeilmolae.global.config.security.jwt.JwtProvider;
import naeilmolae.global.config.security.jwt.TokenInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class KakaoLoginStrategy implements LoginStrategy {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final KakaoMemberClient kakaoMemberClient;
    private final MemberRefreshTokenService memberRefreshTokenService;

    @Override
    public MemberLoginResponseDto login(String accessToken) {
        // Kakao-specific logic
        String clientId = kakaoMemberClient.getClientId(accessToken);
        Optional<Member> getMember = memberRepository.findByClientIdAndLoginType(clientId, LoginType.KAKAO);

        if (getMember.isEmpty()) {
            return saveNewMember(clientId);
        }

        Member member = getMember.get();
        boolean isServiceMember = member.getName() != null;
        TokenInfo tokenInfo = generateToken(member);

        return memberMapper.toMemberLoginResponseDto(member, tokenInfo, isServiceMember, member.getRole());
    }

    private MemberLoginResponseDto saveNewMember(String clientId) {
        Member member = memberMapper.toMember(clientId, LoginType.KAKAO);
        member.changeRole(Role.GUEST);
        Member newMember = memberService.saveEntity(member);
        TokenInfo tokenInfo = generateToken(newMember);
        return memberMapper.toMemberLoginResponseDto(newMember, tokenInfo, false, Role.GUEST);
    }

    private TokenInfo generateToken(Member member) {

        TokenInfo tokenInfo = jwtProvider.generateToken(member.getId().toString(), member.getRole().toString());

        memberRefreshTokenService.saveRefreshToken(tokenInfo.refreshToken(), member);

        return tokenInfo;
    }
}

