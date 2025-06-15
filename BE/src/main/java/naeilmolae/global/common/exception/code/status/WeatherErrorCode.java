package naeilmolae.global.common.exception.code.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum  WeatherErrorCode implements BaseCodeInterface {
    _NO_CONTENT(HttpStatus.INTERNAL_SERVER_ERROR, "GRID_501", "외부 API 요청중 오류가 발생했습니다."),
    _PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GRID_502", "응답 데이터 파싱중 오류가 발생했습니다."),
    _ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GRID_503", "알 수 없는 에러가 발생했습니다.");

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
