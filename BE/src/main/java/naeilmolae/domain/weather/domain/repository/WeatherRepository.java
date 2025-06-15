package naeilmolae.domain.weather.domain.repository;

import naeilmolae.domain.weather.domain.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
