package naeilmolae.domain.voicefile.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "탈퇴 전에 보여주는 객체")
public class RetentionDto {
    private Long voiceCount;
    private Long thanksCount; // 총 청취자 수
    private Long messageCount; // 리액션 타입과 누적 개수
}
