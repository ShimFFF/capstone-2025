package naeilmolae.domain.weather.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.pushnotification.strategy.impl.OutingNotificationStrategy;
import naeilmolae.domain.weather.application.usecase.GridUseCase;
import naeilmolae.domain.weather.application.usecase.WeatherUseCase;
import naeilmolae.domain.weather.domain.entity.Grid;
import naeilmolae.domain.weather.domain.entity.Weather;
import naeilmolae.domain.weather.application.event.WeatherCategorizer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WeatherFetchingScheduler {
    private final WeatherUseCase weatherUseCase;
    private final GridUseCase gridUseCase;
    private final WeatherCategorizer weatherCategorizer;
    private final MemberAdapterService memberAdapterService;
    private final OutingNotificationStrategy outingNotificationStrategy;

    @Scheduled(cron = "0 * * * * *")
    public void fetchWeatherData() {
        List<Grid> grids = gridUseCase.findAll();

        Map<Long, AlarmCategory> map = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        for (Grid grid : grids) {
            List<Weather> weathers = weatherUseCase.requestWeatherData(grid.getX().toString(), grid.getY().toString(), now);
            AlarmCategory categorize = weatherCategorizer.categorize(weathers);
            map.put(grid.getId(), categorize);
        }

        List<Member> allYouthMember = memberAdapterService.getAllYouthMember();
        Map<Long, AlarmCategory> result = new HashMap<>();
        for (Member member : allYouthMember) {
            result.put(member.getId(), map.get(member.getYouthMemberInfo().getGridId()));
        }

        outingNotificationStrategy.updateAlarmCategoryMap(result);
    }
}
