package naeilmolae.domain.voicefile.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import naeilmolae.domain.member.dto.response.SimpleMemberDto;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.domain.entity.ThanksMessage;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "청년의 편지 조회 응답 객체")
public class ProvidedFileResponseDto {

    @Schema(description = "제공된 파일 고유 ID (Long)", example = "1")
    private Long providedFileId;

    @Schema(description = "제공된 파일 생성 시간", example = "2021-08-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "감사 메시지", example = "감사합니다")
    private List<String> thanksMessage;

    @Schema(description = "알람 타입", example = "기상")
    private String alarmType;

    private SimpleMemberDto member;

    public static ProvidedFileResponseDto from(ProvidedFile providedFile, String alarmType, SimpleMemberDto simpleMemberDto) {
        return new ProvidedFileResponseDto(providedFile.getId(),
                providedFile.getCreatedAt(),
                providedFile.getThanksMessages().stream().map(ThanksMessage::getMessage).toList(),
                alarmType,
                simpleMemberDto);
    }
}
