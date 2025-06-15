package naeilmolae.global.common.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum VoiceFileErrorStatus implements BaseCodeInterface {
    _LACK_OF_MESSAGE(HttpStatus.NOT_FOUND , "VOICE001", "제공할 수 있는 응원 음성이 없습니다."),
    _NO_SUCH_FILE(HttpStatus.BAD_REQUEST , "VOICE002", "해당 파일은 존재하지 않습니다."),
    _VOICE_FILE_NOT_PROVIDED(HttpStatus.BAD_REQUEST , "VOICE003", "아직 음성 파일이 제공되지 않은 상태입니다.");
//    _ANALYSIS_NOT_YET(HttpStatus.BAD_REQUEST , "VOICE003", "아직 음성 파일이 제공되지 않은 상태입니다.");

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
