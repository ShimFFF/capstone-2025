package naeilmolae.domain.alarm.domain.repository;

import naeilmolae.domain.alarm.domain.entity.Alarm;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("SELECT a FROM Alarm a " +
            "WHERE a.id = :id")
    Optional<Alarm> findById(Long id);

    @Query("SELECT a FROM Alarm a " +
            "WHERE a.alarmCategory = :alarmCategory")
    Optional<Alarm> findByAlarmCategoryId(AlarmCategory alarmCategory);

    @Query("SELECT a FROM Alarm a WHERE a.alarmCategory IN :categories")
    Set<Alarm> findByCategories(@Param("categories") List<AlarmCategory> categories);

    @Query("SELECT a FROM Alarm a WHERE a.id IN :alarmIds")
    Set<Alarm> findByIdIn(@Param("alarmIds") Set<Long> alarmIds);


}
