package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
    Long countAllByRole(Role role);

    @Query("SELECT m FROM Member m JOIN FETCH m.youthMemberInfo y WHERE m.role = 'YOUTH'")
    List<Member> findAllYouthMembersWithInfo(@Param("role") Role role);

    @Query("SELECT m FROM Member m JOIN FETCH m.helperMemberInfo h WHERE m.role = 'HELPER'")
    List<Member> findAllHelperMembersWithInfo(@Param("role") Role role);

    // 특정 회원이 HelperMemberInfo를 가지고 있는지 확인하는 메서드
    @Query("SELECT m FROM Member m WHERE m.id = :memberId AND m.helperMemberInfo IS NOT NULL")
    Optional<Member> findByIdAndHasHelperInfo(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m WHERE m.id = :memberId AND m.youthMemberInfo IS NOT NULL")
    Optional<Member> findByIdAndHasYouthInfo(@Param("memberId") Long memberId);

    List<Member> findByIdIn(List<Long> ids);

    Optional<Member> findByName(String name);

    Optional<Member> findByRefreshToken(String refreshToken);

    /**
     * 제공된 파일의 ID 목록을 받아 해당 파일을 제공한 회원 정보를 조회
     * @param providedFileIds
     * @return
     */
    @Query("select pf.id, m " +
            "from ProvidedFile pf " +
            "join pf.voiceFile vf " +
            "join Member m on vf.memberId = m.id " +
            "where pf.id in :providedFileIds")
    List<Object[]> findProvidedFileMemberPairs(@Param("providedFileIds") List<Long> providedFileIds);

    @Query("select m from Member m where m.id = (select v.memberId from VoiceFile v where v.id = :voiceFileId)")
    Optional<Member> findMemberByVoiceFileId(@Param("voiceFileId") Long voiceFileId);

}
