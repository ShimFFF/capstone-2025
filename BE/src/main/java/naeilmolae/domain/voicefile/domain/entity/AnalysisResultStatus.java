package naeilmolae.domain.voicefile.domain.entity;

public enum AnalysisResultStatus {
    NOT_READ_VOICE("NOT_READ_VOICE",0), // 음성을 그대로 읽지 않음
    INCLUDE_INAPPROPRIATE_CONTENT("INCLUDE_INAPPROPRIATE_CONTENT", 1), // 부적절한 내용 포함
    SUCCESS("SUCCESS", null),
    ERROR("ERROR", 500);


    private final String description;
    private final Integer reasonCode;

    AnalysisResultStatus(String description, Integer reasonCode) {
        this.description = description;
        this.reasonCode = reasonCode;
    }

    // reasoncode 로만 생성
    public static AnalysisResultStatus of(Integer reasonCode) {
        for (AnalysisResultStatus status : AnalysisResultStatus.values()) {
            if (status.reasonCode.equals(reasonCode)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown reasonCode: " + reasonCode);
    }

    public String getDescription() {
        return description;
    }

    // 입력된 문자열과 매칭되는 enum을 반환하는 메서드
    public static AnalysisResultStatus fromString(String description) {
        for (AnalysisResultStatus status : AnalysisResultStatus.values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + description);
    }

}
