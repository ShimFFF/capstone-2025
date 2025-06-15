package naeilmolae.domain.voicefile.domain.repository;

import naeilmolae.domain.voicefile.domain.entity.VoiceFileReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceFileReportRepository extends JpaRepository<VoiceFileReport, Long> {
}
