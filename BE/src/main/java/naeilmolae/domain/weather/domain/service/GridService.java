package naeilmolae.domain.weather.domain.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.entity.Grid;
import naeilmolae.domain.weather.domain.repository.GridRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GridService {

    private final GridRepository gridRepository;
    @Transactional
    public Grid save(Grid grid) {
        return gridRepository.save(grid);
    }

    public Grid findByPoint(String x, String y) {
        return  gridRepository.findByPoint(x, y)
                .orElseThrow();
    }

    public List<Grid> findAll() {
        return gridRepository.findAll();
    }

}
