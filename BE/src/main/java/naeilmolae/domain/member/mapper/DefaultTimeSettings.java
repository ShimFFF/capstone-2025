package naeilmolae.domain.member.mapper;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DefaultTimeSettings {

    public static final LocalTime DEFAULT_WAKE_UP_TIME = LocalTime.of(8, 0);
    public static final LocalTime DEFAULT_SLEEP_TIME = LocalTime.of(22, 0);
    public static final LocalTime DEFAULT_BREAKFAST_TIME = LocalTime.of(8, 30);
    public static final LocalTime DEFAULT_LUNCH_TIME = LocalTime.of(12, 0);
    public static final LocalTime DEFAULT_DINNER_TIME = LocalTime.of(19, 0);
}
