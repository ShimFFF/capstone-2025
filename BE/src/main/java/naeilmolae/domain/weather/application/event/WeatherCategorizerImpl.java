package naeilmolae.domain.weather.application.event;

import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.weather.domain.entity.Weather;
import naeilmolae.domain.weather.domain.entity.WeatherCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeatherCategorizerImpl implements WeatherCategorizer {
    @Override
    public AlarmCategory categorize(List<Weather> weatherList) {
        boolean isClear = true;
        boolean isRain = false;
        boolean isSnow = false;
        boolean isCold = false;
        boolean isHot = false;

        for (Weather weather : weatherList) {
            WeatherCategory category = weather.getCategory();
            Double value = weather.getValue();

            if (category == WeatherCategory.PTY) {
                // 강수 형태에 따른 분류
                if (value == 1) {
                    isRain = true;
                    isClear = false;
                } else if (value == 2 || value == 3) {
                    isSnow = true;
                    isClear = false;
                }
            } else if (category == WeatherCategory.T1H) {
                // 기온에 따른 분류
                if (value <= 5) {
                    isCold = true;
                } else if (value >= 30) {
                    isHot = true;
                }
            }
        }

        // 우선순위에 따라 AlarmCategory를 반환
        if (isSnow) {
            return AlarmCategory.GO_OUT_SNOW; // 눈이 올 때
        }
        if (isRain) {
            return AlarmCategory.GO_OUT_RAIN; // 비가 올 때
        }
        if (isCold) {
            return AlarmCategory.GO_OUT_COLD; // 추울 때
        }
        if (isHot) {
            return AlarmCategory.GO_OUT_HOT; // 더울 때
        }
        if (isClear) {
            return AlarmCategory.GO_OUT_CLEAR; // 맑을 때
        }

        throw new IllegalStateException("적절한 카테고리를 결정할 수 없습니다.");
    }
}
