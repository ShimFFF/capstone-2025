package naeilmolae.domain.voicefile.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import naeilmolae.domain.voicefile.domain.entity.VoiceReactionType;

import java.util.Map;

@Data
@AllArgsConstructor
@Schema(description = "음성 파일 반응 요약 정보 응답 객체")
public class VoiceFileReactionSummaryResponseDto {
    private Long totalListeners; // 총 청취자 수

    private Map<VoiceReactionType, Integer> reactionsNum; // 리액션 타입과 누적 개수
}
