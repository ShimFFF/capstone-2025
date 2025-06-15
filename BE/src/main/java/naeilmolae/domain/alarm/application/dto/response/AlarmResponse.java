package naeilmolae.domain.alarm.application.dto.response;

import lombok.Getter;
import naeilmolae.domain.alarm.domain.entity.Alarm;

@Getter
public class AlarmResponse {
    private final Long alarmId;
    private final String alarmCategory;
    private final String alarmCategoryKo;

    public AlarmResponse(Alarm alarm) {
        this.alarmId = alarm.getId();
        this.alarmCategory = alarm.getAlarmCategory().name();
        this.alarmCategoryKo = alarm.getAlarmCategory().getName();
    }
}
