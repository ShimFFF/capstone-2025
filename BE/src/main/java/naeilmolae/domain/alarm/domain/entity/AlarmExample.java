package naeilmolae.domain.alarm.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AlarmExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Alarm alarm;

    private String content;

}
