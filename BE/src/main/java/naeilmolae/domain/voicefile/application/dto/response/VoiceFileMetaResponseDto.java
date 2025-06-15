package naeilmolae.domain.voicefile.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.domain.entity.VoiceFileStatus;

@Data
@Schema(description = "음성 파일 메타 정보 응답 객체")
public class VoiceFileMetaResponseDto {

    @Schema(description = "음성 파일 고유 ID (Long)", example = "1")
    private Long voiceFileId;


    @Schema(description = "음성 파일 처리 상태", example = "PROCESSING")
    private VoiceFileStatus process;

    @Schema(description = "스크립트 내용")
    private String content;


    public static VoiceFileMetaResponseDto fromEntity(VoiceFile voiceFile) {
//        if (voiceFile.getAlarm() == null) {
//            throw new IllegalArgumentException("Alarm이 존재하지 않습니다.");
//        }

        VoiceFileMetaResponseDto responseDto = new VoiceFileMetaResponseDto();
        responseDto.voiceFileId = voiceFile.getId();
        responseDto.process = voiceFile.getStatus();
        responseDto.content = voiceFile.getContent();
        return responseDto;
    }
}
