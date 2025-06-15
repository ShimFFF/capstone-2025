package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.domain.HelperMemberInfo;
import naeilmolae.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HelperMemberInfoRepository extends JpaRepository<HelperMemberInfo, Long> {
}
