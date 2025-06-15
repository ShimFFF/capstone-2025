package naeilmolae.domain.weather.application.dto;

import lombok.Getter;
import naeilmolae.domain.weather.domain.entity.Weather;
import naeilmolae.domain.weather.domain.entity.WeatherCategory;

@Getter
public class WeatherDto {
    private Long gridId;
    private WeatherCategory category;
    private Double value;

    public WeatherDto(Weather weather) {
        this.gridId = weather.getGrid().getId();
        this.category = weather.getCategory();
        this.value = weather.getValue();
    }
}
