package naeilmolae.domain.voicefile.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import naeilmolae.domain.member.dto.response.SimpleMemberDto;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "사용 가능한 음성 파일 응답 객체")
@ToString
public class AvailableVoiceFileResponseDto {

    @Schema(description = "음성 파일 고유 ID (Long)", example = "1")
    private Long voiceFileId;

    @Schema(description = "음성 파일 URL", example = "https://example.com/voice.mp3")
    private String fileUrl;

    private String content;

    private Long providedFileId;

    private SimpleMemberDto member;

    public static AvailableVoiceFileResponseDto from(VoiceFile voiceFile, Long providedFileId, SimpleMemberDto simpleMemberDto) {
        AvailableVoiceFileResponseDto availableVoiceFileResponseDto = new AvailableVoiceFileResponseDto();
        availableVoiceFileResponseDto.setVoiceFileId(voiceFile.getId());
        availableVoiceFileResponseDto.setFileUrl(voiceFile.getFileUrl());
        availableVoiceFileResponseDto.setProvidedFileId(providedFileId);
        availableVoiceFileResponseDto.setContent(voiceFile.getContent());
        availableVoiceFileResponseDto.setMember(simpleMemberDto);

        return availableVoiceFileResponseDto;
    }
}
