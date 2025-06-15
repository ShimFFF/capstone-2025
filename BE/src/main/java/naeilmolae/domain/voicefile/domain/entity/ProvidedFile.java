package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ProvidedFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_file_id")
    private VoiceFile voiceFile;

    // 감사 메시지들을 Set으로 관리 (중복 방지, 순서 유지)
    @OneToMany(mappedBy = "providedFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThanksMessage> thanksMessages = new LinkedHashSet<>();

    private Long consumerId;
    private boolean isConsumerSaved = false;

    public ProvidedFile(VoiceFile voiceFile, Long consumerId) {
        this.voiceFile = voiceFile;
        this.consumerId = consumerId;
    }

    /**
     * 감사 메시지 추가 (최대 5개, 중복 금지)
     *
     * @param message 추가할 메시지
     * @return 추가 성공 여부 (true: 추가됨, false: 추가 실패)
     */
    public boolean addThanksMessage(String message) {
        if (thanksMessages.size() >= 5) {
            return false; // 최대 5개 제한
        }
        // 이미 같은 메시지가 있으면 추가하지 않음
        boolean exists = thanksMessages.stream()
                .anyMatch(tm -> tm.getMessage().equals(message));
        if (exists) {
            return false;
        }
        ThanksMessage newMessage = new ThanksMessage(this, message);
        thanksMessages.add(newMessage);
        return true;
    }

    /**
     * 감사 메시지 삭제
     *
     * @param message 삭제할 메시지
     * @return 삭제 성공 여부 (true: 삭제됨, false: 해당 메시지가 없음)
     */
    public boolean removeThanksMessage(String message) {
        ThanksMessage tm = thanksMessages.stream()
                .filter(m -> m.getMessage().equals(message))
                .findFirst()
                .orElse(null);
        if (tm != null) {
            thanksMessages.remove(tm);
            return true;
        }
        return false;
    }

    public boolean setConsumerSaved() {
        this.isConsumerSaved = true;
        return true;
    }


}
