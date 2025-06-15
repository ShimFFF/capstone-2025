package naeilmolae.domain.weather.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    private WeatherCategory category;
    @Column(name = "weather_value")
    private Double value;

    public Weather(Grid grid, WeatherCategory category, Double value) {
        this.grid = grid;
        this.category = category;
        this.value = value;
    }
}
