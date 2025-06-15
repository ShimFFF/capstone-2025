package naeilmolae.global.common.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum AlarmErrorStatus implements BaseCodeInterface {

    _NOT_FOUND(HttpStatus.NOT_FOUND, "ALARM001", "알람을 찾을 수 없습니다."),
    _NOT_FOUND_BY_CATEGORY(HttpStatus.NOT_FOUND, "ALARM002", "해당 카테고리로는 알람을 조회할 수 없습니다. childrenAlarmCategory 로 조회해주세요."),
    _NO_FOUND_MESSAGE(HttpStatus.NOT_FOUND, "ALARM003", "해당 알람의 안내 메시지가 존재하지 않습니다."),
    _NO_FOUND_EXAMPLE_MESSAGE(HttpStatus.NOT_FOUND, "ALARM004", "해당 알람의 예시 메시지가 존재하지 않습니다."),
    _CANNOT_ADD_ROOT_ALARM_CATEGORY(HttpStatus.BAD_REQUEST, "ALARM005", "최상위 카테고리를 저장할 수 없습니다."),;

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
