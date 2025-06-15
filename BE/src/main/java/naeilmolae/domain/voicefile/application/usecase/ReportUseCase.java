package naeilmolae.domain.voicefile.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFileReport;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.domain.entity.VoiceFileReport;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileReportRepository;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileRepository;
import naeilmolae.domain.voicefile.domain.repository.VoiceFileReportRepository;
import naeilmolae.domain.voicefile.domain.repository.VoiceFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.ProvidedFileErrorStatus;
import naeilmolae.global.common.exception.code.status.VoiceFileErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportUseCase {

    private final ProvidedFileReportRepository providedFileReportRepository;
    private final ProvidedFileRepository providedFileRepository;
    private final MemberAdapterService memberAdapterService;

    private final VoiceFileReportRepository voiceFileReportRepository;
    private final VoiceFileRepository voiceFileRepository;

    @Transactional
    public boolean reportProvidedFile(Long reporterId, Long providedFileId, String reason) {
        // 신고할 ProvidedFile 조회
        ProvidedFile providedFile = providedFileRepository.findById(providedFileId)
                .orElseThrow(() -> new RestApiException(ProvidedFileErrorStatus._NOT_FOUND_FILE));

        // 신고 엔티티 생성
        Member member = memberAdapterService.findById(reporterId);
        ProvidedFileReport report = new ProvidedFileReport(providedFile, member, reason);

        // ProvidedFile에 신고 추가
        providedFileReportRepository.save(report);

        return true;
    }

    @Transactional
    public boolean reportVoiceFile(Long reporterId, Long voiceFileId, String reason) {
        VoiceFile voiceFileNotFound = voiceFileRepository.findById(voiceFileId)
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._NO_SUCH_FILE)); // TODO 예외 정의
        Member member = memberAdapterService.findById(reporterId);
        VoiceFileReport report = new VoiceFileReport(voiceFileNotFound, member, reason);
        voiceFileReportRepository.save(report);

        return true;
    }
}
