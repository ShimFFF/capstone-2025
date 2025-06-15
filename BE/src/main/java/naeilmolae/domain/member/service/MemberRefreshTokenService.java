package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.config.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberRefreshTokenService {

    private final JwtProvider jwtProvider;

    @Transactional
    public void saveRefreshToken(String refreshToken, Member member) {
        member.setRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Member member) {
        member.setRefreshToken(null);
    }

    @Transactional(readOnly = true)
    LocalDateTime getLastIssueAt(String refreshToken) {
        return jwtProvider.getIssuedAt(refreshToken);
    }
}

