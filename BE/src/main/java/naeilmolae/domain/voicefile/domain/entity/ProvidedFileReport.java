package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProvidedFileReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provided_file_id", nullable = false)
    private ProvidedFile providedFile;

    public ProvidedFileReport(ProvidedFile providedFile, Member member, String reason) {
        super(member, reason);
        this.providedFile = providedFile;
    }
}
