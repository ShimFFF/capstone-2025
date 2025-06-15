package naeilmolae.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "청년회원 정보 객체")
public class YouthMemberInfoDto {

    @Schema(description = "위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "경도", example = "126.9780")
    private Double longitude;

    @Schema(description = "기상 시간 (HH:mm:ss)", example = "07:30:00")
    private LocalTime wakeUpTime;

    @Schema(description = "취침 시간 (HH:mm:ss)", example = "23:00:00")
    private LocalTime sleepTime;

    @Schema(description = "아침 식사 시간 (HH:mm:ss)", example = "08:00:00")
    private LocalTime breakfast;

    @Schema(description = "점심 식사 시간 (HH:mm:ss)", example = "12:30:00")
    private LocalTime lunch;

    @Schema(description = "저녁 식사 시간 (HH:mm:ss)", example = "19:00:00")
    private LocalTime dinner;
}
