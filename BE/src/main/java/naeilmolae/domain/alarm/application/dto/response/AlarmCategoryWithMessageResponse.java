package naeilmolae.domain.alarm.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.entity.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryWithMessageResponse {
    private AlarmCategory alarmCategory;
    private String alarmCategoryKoreanName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // 비어있는 경우 직렬화 제외
    private String title;
    private CategoryType categoryType;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // 비어있는 경우 직렬화 제외
    private List<AlarmCategoryWithMessageResponse> children = new ArrayList<>();

    public AlarmCategoryWithMessageResponse(AlarmCategory alarmCategory) {
        this.alarmCategory = alarmCategory;
        this.alarmCategoryKoreanName = alarmCategory.getName();
        this.title = alarmCategory.getTitle();
        this.categoryType = alarmCategory.getCategoryType();
    }

    public void addChildren(AlarmCategoryWithMessageResponse child) {
        this.children.add(child);
    }
}
