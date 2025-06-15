package naeilmolae.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "청년회원 정보 업데이트 객체")
public class YouthMemberInfoUpdateDto {

    @Schema(description = "기상 시간", example = "07:30:00")
    private LocalTime wakeUpTime;

    @Schema(description = "취침 시간", example = "23:00:00")
    private LocalTime sleepTime;

    @Schema(description = "아침 식사 시간", example = "08:00:00")
    private LocalTime breakfast;

    @Schema(description = "점심 식사 시간", example = "12:30:00")
    private LocalTime lunch;

    @Schema(description = "저녁 식사 시간", example = "19:00:00")
    private LocalTime dinner;

    @Schema(description = "외출 시간", example = "14:00:00")
    private LocalTime outgoingTime;
}