package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.service.AlarmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmUseCase {
    private final AlarmService alarmService;

    // 알람 조회
    public AlarmResponse findById(Long id) {
        return new AlarmResponse(alarmService.findById(id));
    }

    // 알람 ID 리스트로 알람 조회
    public Set<AlarmResponse> findByIdIn(Set<Long> alarmIds) {
        return alarmService.findByIdIn(alarmIds)
                .stream()
                .map(AlarmResponse::new)
                .collect(Collectors.toSet());
    }


    // 알람 카테고리 ID로 알람 조회
    public AlarmResponse findByAlarmCategory(AlarmCategory alarmCategory) {
        return new AlarmResponse(alarmService.findByAlarmCategory(alarmCategory));
    }

    public Set<AlarmResponse> findByChildrenCategories(List<AlarmCategory> alarmCategories) {
        return alarmService.findByCategories(alarmCategories)
                .stream()
                .map(AlarmResponse::new)
                .collect(Collectors.toSet());
    }
}
