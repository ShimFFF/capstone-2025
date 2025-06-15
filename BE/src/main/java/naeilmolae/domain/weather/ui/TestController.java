package naeilmolae.domain.weather.ui;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.entity.Grid;
import naeilmolae.domain.weather.application.dto.GridDto;
import naeilmolae.domain.weather.application.dto.WeatherDto;
import naeilmolae.domain.weather.application.usecase.GridUseCase;
import naeilmolae.domain.weather.application.usecase.WeatherUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequestMapping("/api/weather/grid")
@RequiredArgsConstructor
public class TestController {
    private final GridUseCase gridUseCase;
    private final WeatherUseCase weatherUseCase;

    /**
     * 위도, 경도를 받아서 그리드 좌표를 반환합니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 그리드 좌표
     */
    @GetMapping
    public ResponseEntity<Grid> getGridCoordinates(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        Grid grid = gridUseCase.getGridCoordinates(latitude, longitude);
        return ResponseEntity.ok(grid);
    }

    @PostMapping("/data")
    public ResponseEntity<List<WeatherDto>> requestWeatherData(
            @RequestParam("gridX") Integer gridX,
            @RequestParam("gridY") Integer gridY,
            @RequestParam("dateTime") String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        GridDto gridDto = new GridDto(gridX, gridY); // Grid 객체 생성

        List<WeatherDto> collect = weatherUseCase.requestWeatherData(gridDto.getX().toString(),
                        gridDto.getY().toString(),
                        localDateTime)
                .stream()
                .map(WeatherDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }
}
