package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoiceFileReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_file_id", nullable = false)
    private VoiceFile voiceFile;

    public VoiceFileReport(VoiceFile voiceFile, Member member, String reason) {
        super(member, reason);
        this.voiceFile = voiceFile;
    }
}
