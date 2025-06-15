package naeilmolae.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.global.common.base.BaseEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YouthMemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime wakeUpTime;

    private boolean wakeUpAlarm = true;
    private LocalTime sleepTime;

    private boolean sleepAlarm = true;
    private LocalTime breakfast;

    private boolean breakfastAlarm = true;
    private LocalTime lunch;

    private boolean lunchAlarm = true;
    private LocalTime dinner;

    private boolean dinnerAlarm = true;

    private LocalTime outgoingTime = LocalTime.of(8, 30);

    private boolean outgoingAlarm = true;

    private Double latitude; // 위도

    private Double longitude; // 경도

    private Long gridId;


    @Builder
    public YouthMemberInfo(
            LocalTime wakeUpTime, LocalTime sleepTime,
            LocalTime breakfast, LocalTime lunch, LocalTime dinner,
            Double latitude, Double longitude, Long gridId) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gridId = gridId;
    }

    // 빈값이거나 없는 값이 아닌 경우에만 업데이트
    public void updateYouthMemberInfoDto(YouthMemberInfoDto youthMemberInfoDto) {
        Optional.ofNullable(youthMemberInfoDto.getWakeUpTime()).ifPresent(value -> this.wakeUpTime = value);
        Optional.ofNullable(youthMemberInfoDto.getSleepTime()).ifPresent(value -> this.sleepTime = value);
        Optional.ofNullable(youthMemberInfoDto.getBreakfast()).ifPresent(value -> this.breakfast = value);
        Optional.ofNullable(youthMemberInfoDto.getLunch()).ifPresent(value -> this.lunch = value);
        Optional.ofNullable(youthMemberInfoDto.getDinner()).ifPresent(value -> this.dinner = value);
        Optional.ofNullable(youthMemberInfoDto.getLatitude()).ifPresent(value -> this.latitude = value);
        Optional.ofNullable(youthMemberInfoDto.getLongitude()).ifPresent(value -> this.longitude = value);
    }

    // 빈값이거나 없는 값이 아닌 경우에만 업데이트
    public void updateYouthMemberInfoDto(YouthMemberInfoUpdateDto youthMemberInfoDto) {
        Optional.ofNullable(youthMemberInfoDto.getWakeUpTime()).ifPresent(value -> this.wakeUpTime = value);
        Optional.ofNullable(youthMemberInfoDto.getSleepTime()).ifPresent(value -> this.sleepTime = value);
        Optional.ofNullable(youthMemberInfoDto.getBreakfast()).ifPresent(value -> this.breakfast = value);
        Optional.ofNullable(youthMemberInfoDto.getLunch()).ifPresent(value -> this.lunch = value);
        Optional.ofNullable(youthMemberInfoDto.getDinner()).ifPresent(value -> this.dinner = value);
        Optional.ofNullable(youthMemberInfoDto.getOutgoingTime()).ifPresent(value -> this.outgoingTime = value);
    }


//    public void setGrid(Double gridX, Double gridY) {
//        this.gridX = gridX;
//        this.gridY = gridY;
//    }
}
