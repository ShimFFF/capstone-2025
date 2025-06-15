package naeilmolae.global.common.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.global.common.exception.code.BaseCodeDto;
import naeilmolae.global.common.exception.code.BaseCodeInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AnalysisErrorStatus implements BaseCodeInterface {
    // 음성 분석 관련 에러
    _NOT_YET(HttpStatus.OK, "ANALYSIS001", "아직 분석 중입니다."),
    _INCLUDE_INAPPROPRIATE_CONTENT(HttpStatus.OK, "ANALYSIS002", "부적절한 내용이 포함되어 있습니다."),
    _NOT_READ_VOICE(HttpStatus.OK, "ANALYSIS003", "텍스트를 그대로 읽지 않았습니다."),
    _ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS004", "분석 중 에러가 발생하였습니다."),
    _ERROR_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS006", "정의되지 않은 예외입니다."),
    _GPT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS108", "GPT가 올바르지 않은 답변을 했습니다. 관리자에게 문의하세요."),

    // 스크립트 관련 에러
    _INVALID_CONTEXT(HttpStatus.OK, "ANALYSIS200", "제출된 상황에 맞지 않은 스크립트입니다."),
    _PROFANITY_DETECTED(HttpStatus.OK, "ANALYSIS201", "불쾌한 표현이 포함되어 있습니다.");

//    _CANNOT_SAVE_ANALYSIS_RESULT(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS005", "분석 결과 저장에 문제가 발생했습니다."),
//    _DENIED_BY_GPT(HttpStatus.OK, "ANALYSIS107", "GPT: 올바르지 않은 스크립트입니다."),

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
