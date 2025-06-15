package naeilmolae.domain.weather.domain.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.entity.Weather;
import naeilmolae.domain.weather.domain.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public List<Weather> saveAll(List<Weather> weathers) {
        return weatherRepository.saveAll(weathers);
    }
}
