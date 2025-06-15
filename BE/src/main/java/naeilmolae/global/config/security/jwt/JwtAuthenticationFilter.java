package naeilmolae.global.config.security.jwt;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.member.service.MemberServiceImpl;
import naeilmolae.global.config.security.auth.PrincipalDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    // 오직 인증 정보를 설정하는 역할만 수행

    private final JwtProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request); // 헤더에서 토큰을 받아옴

        if (token != null && jwtTokenProvider.validateToken(token)) { // 토큰이 유효하다면
            Authentication authentication = getAuthentication(token); // 인증 정보를 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보를 설정
        }
        chain.doFilter(request, response); // 다음 필터로 넘김
    }

    private Authentication getAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String role = claims.get("role", String.class);

        Long memberId = Long.valueOf(claims.getSubject());
        Member member = memberService.findById(memberId); // Member 객체 조회

        // PrincipalDetails 사용
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }
}
