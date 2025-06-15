package naeilmolae.domain.alarm.application.converter;

import naeilmolae.domain.alarm.domain.entity.CategoryType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component

public class StringToCategoryTypeConverter implements Converter<String, CategoryType> {
    @Override
    public CategoryType convert(String source) {
        return CategoryType.valueOf(source.toUpperCase());
    }
}
