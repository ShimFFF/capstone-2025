package naeilmolae.domain.weather.application.event;

import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.weather.domain.entity.Weather;

import java.util.List;

public interface WeatherCategorizer {
    AlarmCategory categorize(List<Weather> weatherList);
}
