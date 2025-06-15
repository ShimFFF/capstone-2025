package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.AlarmExample;
import naeilmolae.domain.alarm.domain.repository.AlarmExampleRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AlarmErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmExampleUseCase {
    private final AlarmExampleRepository alarmExampleRepository;

    public AlarmExample findAllByAlarmId(Long alarmId) {
        List<AlarmExample> allByAlarmId = alarmExampleRepository.findAllByAlarmId(alarmId);

        if (allByAlarmId.isEmpty()) {
            throw new RestApiException(AlarmErrorStatus._NO_FOUND_EXAMPLE_MESSAGE);
        }

        return allByAlarmId.stream()
                .skip(ThreadLocalRandom.current().nextInt(allByAlarmId.size()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unexpected error when selecting random alarm example"));
    }
}
