package naeilmolae.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelperMemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private boolean isWelcomeReminder = true;
    @Setter
    private boolean isThankYouMessage = true;

    public HelperMemberInfo(boolean isWelcomeReminder, boolean isThankYouMessage) {
        this.isWelcomeReminder = isWelcomeReminder;
        this.isThankYouMessage = isThankYouMessage;
    }
}
