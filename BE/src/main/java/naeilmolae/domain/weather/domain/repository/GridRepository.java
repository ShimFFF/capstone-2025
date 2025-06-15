package naeilmolae.domain.weather.domain.repository;

import naeilmolae.domain.weather.domain.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GridRepository extends JpaRepository<Grid, Long> {

    @Query("SELECT g FROM Grid g WHERE g.x = :x AND g.y= :y")
    Optional<Grid> findByPoint(String x, String y);
}
