package naeilmolae.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "청년회원 정보 객체")
public class YouthMemberPushTimeInfo {
    private LocalDateTime wakeUpTime;
    private LocalDateTime sleepTime;
    private LocalDateTime breakfast;
    private LocalDateTime lunch;
    private LocalDateTime dinner;
}
