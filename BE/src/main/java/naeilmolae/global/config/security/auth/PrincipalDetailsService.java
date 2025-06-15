package naeilmolae.global.config.security.auth;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.member.service.MemberServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberService detailMemberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try { //username이 의미 하는 것은 엔티티의 id
            // 그래서 Long으로 변환
            Long userId = Long.parseLong(username); // Long으로 변환
            Member memberEntity = detailMemberService.findById(userId); // 서비스에서 회원 조회
            return new PrincipalDetails(memberEntity); // PrincipalDetails 반환
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid ID format", e); // 잘못된 ID 형식 예외 처리
        }
    }
}
