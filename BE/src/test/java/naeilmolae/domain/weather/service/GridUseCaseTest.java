package naeilmolae.domain.weather.service;

import naeilmolae.domain.weather.domain.entity.Grid;
import naeilmolae.domain.weather.application.usecase.GridUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GridUseCaseTest {
    @Autowired
    private GridUseCase gridUseCase;

    @Test
    void getGridCoordinates() {
        // given
        Double latitude = 37.5665;
        Double longitude = 126.9780;

        // when
        Grid grid = gridUseCase.getGridCoordinates(latitude, longitude);
//
//        // then
//        assertNotNull(grid);
//        assertEquals(grid.getX(), 60);
//        assertEquals(grid.getY(), 127);
    }
}