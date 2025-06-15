package naeilmolae.domain.alarm.application.converter;

import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component

public class StringToAlarmCategoryConverter implements Converter<String, AlarmCategory> {
    @Override
    public AlarmCategory convert(String source) {
        return AlarmCategory.valueOf(source.toUpperCase());
    }
}
