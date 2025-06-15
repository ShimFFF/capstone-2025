package naeilmolae.domain.alarm.domain.repository;

import naeilmolae.domain.alarm.domain.entity.AlarmExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmExampleRepository extends JpaRepository<AlarmExample, Long> {

    @Query("SELECT ae FROM AlarmExample ae WHERE ae.alarm.id = :alarmId")
    List<AlarmExample> findAllByAlarmId(Long alarmId);
}
