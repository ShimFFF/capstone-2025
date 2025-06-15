package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;


@Entity
@Getter
@NoArgsConstructor
public class AnalysisResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "voice_file_id")
    private VoiceFile voiceFile;

    @Enumerated(EnumType.STRING)
    private AnalysisResultStatus analysisResultStatus;

    @Column(nullable = false)
    private String sttContent; // 변환된 텍스트

    public AnalysisResult(VoiceFile voiceFile, AnalysisResultStatus analysisFailureReason, String sttContent) {
        this.voiceFile = voiceFile;
        if (analysisFailureReason == null) {
            throw new IllegalArgumentException("analysisFailureReason must not be null");
        }
        this.analysisResultStatus = analysisFailureReason;
        this.sttContent = sttContent;
    }
}
