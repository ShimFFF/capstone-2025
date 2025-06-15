package naeilmolae.domain.alarm.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;

import java.util.List;

@Getter
public class AlarmCategoryCountResponse {
    private final AlarmCategory alarmCategory;
    private final String koreanName;
    @Setter
    private String title;
    private boolean isUsed = false;
    private final List<AlarmCategory> children;

    public AlarmCategoryCountResponse(AlarmCategory alarmCategory) {
        this.alarmCategory = alarmCategory;
        this.koreanName = alarmCategory.getName();
        this.children = AlarmCategory.getChildrenByParent(alarmCategory);
        this.title = alarmCategory.getTitle();
    }
    public void setUsed() {
        this.isUsed = true;
    }

}
