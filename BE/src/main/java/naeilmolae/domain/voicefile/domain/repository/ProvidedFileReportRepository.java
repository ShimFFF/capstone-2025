package naeilmolae.domain.voicefile.domain.repository;

import naeilmolae.domain.voicefile.domain.entity.ProvidedFileReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvidedFileReportRepository extends JpaRepository<ProvidedFileReport, Long> {
}
