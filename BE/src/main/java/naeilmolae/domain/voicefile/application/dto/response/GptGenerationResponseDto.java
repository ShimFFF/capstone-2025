package naeilmolae.domain.voicefile.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GptGenerationResponseDto {
    private Long alarmId;
    private String content;
}
