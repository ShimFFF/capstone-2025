package naeilmolae.domain.alarm.domain.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryType {
    DAILY("일상"),
    COMFORT("위로"),
    INFO("정보");

    private final String description;
}
