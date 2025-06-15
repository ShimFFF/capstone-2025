package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;

import static naeilmolae.domain.voicefile.domain.entity.VoiceFileStatus.AUDIO_SUBMITTED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_voicefile_member", columnList = "member_id"),
        @Index(name = "idx_voicefile_alarm", columnList = "alarm_id")
})
public class VoiceFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 대용량 텍스트

    @Column(unique = true)
    private String fileUrl; // 저장된 음성 url

    @Enumerated(EnumType.STRING)
    private VoiceFileStatus status = VoiceFileStatus.TEXT_SUBMITTED;

    @OneToOne(mappedBy = "voiceFile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "analysis_result_id")
    private AnalysisResult analysisResult;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "alarm_id")
    private Long alarmId;

    public VoiceFile(Long memberId, Long alarmId, String content) {
        this.content = content;
        this.memberId = memberId;
        this.alarmId = alarmId;
    }

    /**
     * 사용자가 읽을 텍스트 파일 변경
     *
     * @param content 변경할 텍스트
     */
    public void changeContent(String content) {
        this.content = content;
    }

    /**
     * 음성 파일 저장하기, 이후 분석 진행
     *
     * @param fileUrl 저장된 음성 파일 ID
     */
    public void saveFileUrl(String fileUrl) {
        if (this.content == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }

        this.fileUrl = fileUrl;
        this.status = AUDIO_SUBMITTED;
    }

    public void prepareAnalysis() {
        if (this.status != AUDIO_SUBMITTED) {
            throw new IllegalStateException("분석 요청은 녹음 완료 상태에서만 가능합니다.");
        }
        if (this.getFileUrl() == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
    }

    public AnalysisResult saveResult(AnalysisResultStatus analysisResultStatus, String sttContent) {
        // 결과 저장
        AnalysisResult analysisResult = new AnalysisResult(this,
                analysisResultStatus,
                sttContent);
        this.analysisResult = analysisResult;

        return analysisResult;
    }
}
