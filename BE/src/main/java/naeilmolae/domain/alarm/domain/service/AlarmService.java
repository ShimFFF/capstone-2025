package naeilmolae.domain.alarm.domain.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.Alarm;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.repository.AlarmRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AlarmErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor()
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Alarm findById(Long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new RestApiException(AlarmErrorStatus._NOT_FOUND));
    }

    public Set<Alarm> findByIdIn(Set<Long> alarmIds) {
        return alarmRepository.findByIdIn(alarmIds);
    }

    public Alarm findByAlarmCategory(AlarmCategory alarmCategory) {
        return alarmRepository.findByAlarmCategoryId(alarmCategory)
                .orElseThrow(() -> new RestApiException(AlarmErrorStatus._NOT_FOUND_BY_CATEGORY));
    }

    public Set<Alarm> findByCategories(List<AlarmCategory> alarmCategories) {
        return alarmRepository.findByCategories(alarmCategories);
    }

}
