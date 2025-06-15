package naeilmolae.global.config.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.domain.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor // 생성자를 만들어줌
public class PrincipalDetails implements UserDetails {
    private Member member;
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // ROLE_USER, ROLE_ADMIN에 따라 권한 설정하기 위해, authorities에 추가
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
        return authorities;
    }

    @Override                    // 소셜 로그인만 지원해서 pwd 필요 없음
    public String getPassword() {
        // 소셜 로그인만 지원하므로 비밀번호가 필요하지 않음
        return null;
        //return encodePwd().encode("this is password");
    }

    @Override
    public String getUsername() {
        // member의 ID를 username으로 사용 (PrincipalDetailsService에서 long으로 변환)
        return member.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getDeletedAt() == null;
    }


}
