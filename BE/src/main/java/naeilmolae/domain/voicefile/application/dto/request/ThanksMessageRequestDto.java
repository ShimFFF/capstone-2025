package naeilmolae.domain.voicefile.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ThanksMessageRequestDto(
        @Schema(description = "감사 메시지 내용", example = "Thank you for your support!")
        String message
) {
}