package naeilmolae.domain.voicefile.domain.repository;


import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.domain.entity.ThanksMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvidedFileRepository extends JpaRepository<ProvidedFile, Long> {

    // consumerId, VoiceFileId로 파일 조회
    Optional<ProvidedFile> findByConsumerIdAndVoiceFileId(Long consumerId, Long voiceFileId);

    @Query("SELECT pf FROM ProvidedFile pf JOIN FETCH pf.voiceFile WHERE pf.id = :id")
    Optional<ProvidedFile> findById(Long id);

    @Query("""
            SELECT p 
            FROM ProvidedFile p 
            LEFT JOIN FETCH p.thanksMessages 
            WHERE p.consumerId = :consumerId 
            AND p.id = :providedFileId""")
    Optional<ProvidedFile> findByConsumerId(Long consumerId, Long providedFileId);

    @Query("SELECT tm " +
            "FROM ProvidedFile pf " +
            "JOIN pf.thanksMessages tm " +
            "JOIN pf.voiceFile vf " +
            "WHERE vf.memberId = :memberId")
    List<ThanksMessage> findThankMessagesByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(pf) " +
            "FROM ProvidedFile pf " +
            "JOIN pf.voiceFile vf " +
            "WHERE vf.memberId = :memberId")
    Long findTotalListenersByMemberId(@Param("memberId") Long memberId);

    @Query(value = """
        select pf 
        from ProvidedFile pf 
        JOIN FETCH pf.voiceFile pfv
        where pf.voiceFile.memberId = :memberId 
        and pf.voiceFile.alarmId in :alarmIds
        and not exists (
            select 1 
            from ProvidedFileReport pfr 
            where pfr.providedFile = pf
        )
        """,
            countQuery = """
        select count(pf) 
        from ProvidedFile pf 
        where pf.voiceFile.memberId = :memberId
        and pf.voiceFile.alarmId in :alarmIds
        and not exists (
            select 1 
            from ProvidedFileReport pfr 
            where pfr.providedFile = pf
        )
        """)
    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, List<Long> alarmIds, Pageable pageable);

    @Query(value = """
        SELECT pf 
        FROM ProvidedFile pf
        LEFT JOIN pf.voiceFile vf
        LEFT JOIN ProvidedFileReport pr ON pr.providedFile = pf
        WHERE vf.memberId = :memberId
        AND pr.id IS NULL
        """,
            countQuery = """
        SELECT COUNT(pf) 
        FROM ProvidedFile pf
        LEFT JOIN pf.voiceFile vf
        LEFT JOIN ProvidedFileReport pr ON pr.providedFile = pf
        WHERE vf.memberId = :memberId
        AND pr.id IS NULL
        """)
    @EntityGraph(attributePaths = {"voiceFile"})  // voiceFile을 함께 가져옴
    Page<ProvidedFile> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);



}
