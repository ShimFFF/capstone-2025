package naeilmolae.domain.member.strategy.impl;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.member.strategy.LoginStrategy;
import naeilmolae.global.config.security.jwt.JwtProvider;
import naeilmolae.global.config.security.jwt.TokenInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnonymousLoginStrategy implements LoginStrategy {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Override
    public MemberLoginResponseDto login(String accessToken) {
        // Anonymous-specific logic
        Optional<Member> getMember = memberRepository.findByClientIdAndLoginType(accessToken, LoginType.ANOYMOUS);

        if (getMember.isEmpty()) {
            return saveNewMember(accessToken, LoginType.ANOYMOUS);
        }

        Member member = getMember.get();
        boolean isServiceMember = member.getName() != null;
        TokenInfo tokenInfo = generateToken(member);

        return memberMapper.toMemberLoginResponseDto(member, tokenInfo, isServiceMember, member.getRole());
    }

    private MemberLoginResponseDto saveNewMember(String clientId, LoginType loginType) {
        Member member = memberMapper.toMember(clientId, loginType);
        member.changeRole(Role.GUEST);
        Member newMember = memberService.saveEntity(member);
        TokenInfo tokenInfo = generateToken(newMember);
        return memberMapper.toMemberLoginResponseDto(newMember, tokenInfo, false, Role.GUEST);
    }

    private TokenInfo generateToken(Member member) {
        return jwtProvider.generateToken(member.getId().toString(), member.getRole().toString());
    }
}

