package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.entity.AlarmExample;
import naeilmolae.domain.alarm.application.dto.response.AlarmCategoryMessageResponse;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmAdapterUseCase {

    private final AlarmUseCase alarmUsecase;
    private final AlarmCategoryUseCase alarmCategoryUseCase;
    private final AlarmExampleUseCase alarmExampleUseCase;

    // Root Alarm Category 로 알람 조회
    public Set<AlarmResponse> findAlarmIdsByAlarmCategory(String rootAlarmCategory) {
        AlarmCategory category = AlarmCategory.valueOf(rootAlarmCategory.toUpperCase());
        List<AlarmCategory> childrenByParent = AlarmCategory.getChildrenByParent(category);
        return alarmUsecase.findByChildrenCategories(childrenByParent);
    }


    public AlarmCategoryMessageResponse findById(Long alarmId) {
        AlarmResponse alarmResponseDto = alarmUsecase.findById(alarmId);
        AlarmCategory alarmCategory = alarmCategoryUseCase.findByString(alarmResponseDto.getAlarmCategory());
        return new AlarmCategoryMessageResponse(alarmResponseDto.getAlarmId(), alarmResponseDto.getAlarmCategory(), alarmCategory.getTitle());
    }


    public Map<Long, AlarmResponse> findAlarmsByIds(Set<Long> alarmIds) {
        return alarmUsecase.findByIdIn(alarmIds)
                .stream()
                .collect(Collectors.toMap(AlarmResponse::getAlarmId, alarmResponseDto -> alarmResponseDto));
    }

    public AlarmExample findAllByAlarmId(Long alarmId) {
        return alarmExampleUseCase.findAllByAlarmId(alarmId);
    }
}
