package naeilmolae.domain.voicefile.domain.repository;

import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoiceFileRepository extends JpaRepository<VoiceFile, Long> {
    Optional<VoiceFile> findByMemberIdAndId(Long memberId, Long fileId);

    @Query("SELECT vf " +
            "FROM VoiceFile vf " +
            "LEFT JOIN vf.analysisResult as vfas " +
            "WHERE vf.alarmId = :alarmId " +
            "AND vfas.analysisResultStatus = 'SUCCESS'" +
            "AND NOT EXISTS ( " +
            "    SELECT pf " +
            "    FROM ProvidedFile pf " +
            "    WHERE pf.voiceFile = vf " +
            "    AND pf.consumerId = :memberId " +
            ") " +
//            "AND vf.createdAt >= :oneWeekAgo " + // 최근 1주일 이내 데이터만
            "ORDER BY vf.createdAt ASC")
    List<VoiceFile> findUnprovided(
            @Param("memberId") Long memberId,
            @Param("alarmId") Long alarmId);
//            @Param("oneWeekAgo") LocalDateTime oneWeekAgo);

    @Query("""
            SELECT vf.alarmId
            FROM VoiceFile vf 
            WHERE vf.memberId = :memberId 
            AND vf.createdAt >= :startDay 
            AND vf.createdAt < :endDay""")
    List<Long> findAlarmIdsByMemberIdAndBetween(@Param("memberId") Long memberId,
                                        @Param("startDay") LocalDateTime startDay,
                                        @Param("endDay") LocalDateTime endDay);

    Long countByMemberId(Long memberId);
}
