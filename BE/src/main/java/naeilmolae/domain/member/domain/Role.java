package naeilmolae.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("관리자", 0),
    YOUTH("청년", 1),
    HELPER("조력자", 1),
    GUEST("회원가입중", 2),
    WITHDRAWN("탈퇴회원", 3),;

    private final String toKorean;
    private final int priority;
}
