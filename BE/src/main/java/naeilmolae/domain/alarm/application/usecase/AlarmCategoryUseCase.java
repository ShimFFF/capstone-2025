package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.service.AlarmCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmCategoryUseCase {

    private final AlarmCategoryService alarmCategoryService;
    public AlarmCategory findByString(String alarmCategory) {
        return alarmCategoryService.findByString(alarmCategory);
    }

    public List<AlarmCategory> findByStringIn(List<String> alarmCategories) {
        return alarmCategoryService.findByStringIn(alarmCategories);
    }
}
