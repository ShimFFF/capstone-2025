package naeilmolae.domain.member.strategy.impl;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.client.AppleMemberClient;
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
public class AppleLoginStrategy implements LoginStrategy {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final AppleMemberClient appleMemberClient;
    private final MemberRefreshTokenService memberRefreshTokenService;

    @Override
    public MemberLoginResponseDto login(String accessToken) {
        // apple-specific logic
        String clientId = appleMemberClient.getClientId(accessToken);
        Optional<Member> getMember = memberRepository.findByClientIdAndLoginType(clientId, LoginType.APPLE);

        if (getMember.isEmpty()) {
            return saveNewMember(clientId);
        }

        Member member = getMember.get();
        boolean isServiceMember = member.getName() != null;
        TokenInfo tokenInfo = generateToken(member);

        return MemberMapper.toMemberLoginResponseDto(member, tokenInfo, isServiceMember, member.getRole());
    }

    private MemberLoginResponseDto saveNewMember(String clientId) {
        Member member = MemberMapper.toMember(clientId, LoginType.APPLE);
        member.changeRole(Role.GUEST);
        Member newMember = memberService.saveEntity(member);
        TokenInfo tokenInfo = generateToken(newMember);
        return MemberMapper.toMemberLoginResponseDto(newMember, tokenInfo, false, Role.GUEST);
    }

    private TokenInfo generateToken(Member member) {

        TokenInfo tokenInfo = jwtProvider.generateToken(member.getId().toString(), member.getRole().toString());

        memberRefreshTokenService.saveRefreshToken(tokenInfo.refreshToken(), member);

        return tokenInfo;
    }
}