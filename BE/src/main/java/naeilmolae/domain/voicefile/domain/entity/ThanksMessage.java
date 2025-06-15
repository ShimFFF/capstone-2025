package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class ThanksMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provided_file_id")
    private ProvidedFile providedFile;

    private String message;

    public ThanksMessage(ProvidedFile providedFile, String message) {
        this.providedFile = providedFile;
        this.message = message;
    }
}
