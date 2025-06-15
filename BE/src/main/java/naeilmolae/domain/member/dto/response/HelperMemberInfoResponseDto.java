package naeilmolae.domain.member.dto.response;

import lombok.Data;
import lombok.ToString;
import naeilmolae.domain.member.domain.HelperMemberInfo;

@Data
@ToString
public class HelperMemberInfoResponseDto {
    private boolean isWelcomeReminder;
    private boolean isThankYouMessage;

    public static HelperMemberInfoResponseDto of(HelperMemberInfo helperMemberInfo) {
        HelperMemberInfoResponseDto helperMemberInfoResponseDto = new HelperMemberInfoResponseDto();
        helperMemberInfoResponseDto.setWelcomeReminder(helperMemberInfo.isWelcomeReminder());
        helperMemberInfoResponseDto.setThankYouMessage(helperMemberInfo.isThankYouMessage());
        return helperMemberInfoResponseDto;
    }
}
