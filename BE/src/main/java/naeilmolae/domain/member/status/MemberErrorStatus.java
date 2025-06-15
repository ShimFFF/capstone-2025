package naeilmolae.domain.member.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorStatus implements BaseCodeInterface {

    EMPTY_MEMBER(HttpStatus.NOT_FOUND, "MEMBER404", "회원을 찾을 수 없습니다."),
    INVALID_HELPER_AGES(HttpStatus.BAD_REQUEST, "MEMBER400", "조력자의 나이는 성인이여야 합니다.")
    , NOT_HELPER(HttpStatus.BAD_REQUEST, "MEMBER400", "봉사자 역할이 아닙니다.")
    , NOT_YOUTH(HttpStatus.BAD_REQUEST, "MEMBER400", "청년 역할이 아닙니다.")
    , EMPTY_HELPER_INFO(HttpStatus.NOT_FOUND, "MEMBER404", "봉사자 정보가 없습니다.")
    ;

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