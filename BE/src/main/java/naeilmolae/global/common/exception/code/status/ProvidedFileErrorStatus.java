package naeilmolae.global.common.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProvidedFileErrorStatus implements BaseCodeInterface {
    _NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "PF001", "사용자에게 제공되지 않은 파일입니다."),
    _ALREADY_PROVIDED(HttpStatus.BAD_REQUEST, "PF002", "이미 제공된 파일입니다."),
    _EXCEED_MESSAGE(HttpStatus.BAD_REQUEST, "PF003", "감사 메시지는 최대 5개입니다."),;

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
