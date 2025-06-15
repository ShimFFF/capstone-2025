package naeilmolae.domain.alarm.application.dto.response;

import lombok.Getter;
import naeilmolae.domain.alarm.domain.entity.CategoryType;

@Getter
public class CategoryTypeResponse {
    private final CategoryType categoryType;
    private final String koreanName;

    public CategoryTypeResponse(CategoryType categoryType) {
        this.categoryType = categoryType;
        this.koreanName = categoryType.getDescription();
    }
}
