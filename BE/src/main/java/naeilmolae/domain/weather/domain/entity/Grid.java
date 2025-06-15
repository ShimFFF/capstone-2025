package naeilmolae.domain.weather.domain.entity;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"x", "y"})
})
public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer x;
    private Integer y;

    public Grid(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
