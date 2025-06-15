package naeilmolae.domain.alarm.application.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.entity.CategoryType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmCategoryResponse {
    private String alarmCategory;
    private String alarmCategoryKoreanName;
    private CategoryType categoryType;

    public AlarmCategoryResponse from(AlarmCategory alarmCategory) {
        return new AlarmCategoryResponse(alarmCategory.name(), alarmCategory.getName(), alarmCategory.getCategoryType());
    }
}
