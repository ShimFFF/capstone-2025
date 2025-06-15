package naeilmolae.global.common.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus  implements BaseCodeInterface {
    // For test
    _OK(HttpStatus.OK, "COMMON200", "성공입니다.")
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = true;
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
