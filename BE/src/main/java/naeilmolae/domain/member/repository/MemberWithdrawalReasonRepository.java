package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.domain.MemberWithdrawalReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithdrawalReasonRepository extends JpaRepository<MemberWithdrawalReason, Long> {
}
