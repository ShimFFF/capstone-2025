package naeilmolae.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWithdrawalReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    public MemberWithdrawalReason(String reason) {
        this.reason = reason;
    }
}
