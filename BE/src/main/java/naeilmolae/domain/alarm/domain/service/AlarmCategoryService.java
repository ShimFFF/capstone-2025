package naeilmolae.domain.alarm.domain.service;

import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmCategoryService {

    public AlarmCategory findByString(String alarmCategory) {
        return AlarmCategory.valueOf(alarmCategory.toUpperCase());
    }

    public List<AlarmCategory> findByStringIn(List<String> alarmCategories) {
        return alarmCategories.stream()
                .map(alarmCategory -> AlarmCategory.valueOf(alarmCategory.toUpperCase()))
                .toList();
    }
}
