package naeilmolae.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import naeilmolae.domain.member.domain.YouthMemberInfo;

import java.time.LocalTime;

@Getter
@Builder
public class YouthMemberInfoResponseDto {
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private LocalTime breakfast;
    private LocalTime lunch;
    private LocalTime dinner;
    private LocalTime outgoingTime;
    private boolean wakeUpAlarm;
    private boolean sleepAlarm ;
    private boolean breakfastAlarm;
    private boolean lunchAlarm;
    private boolean dinnerAlarm;
    private boolean outgoingAlarm;

    public static YouthMemberInfoResponseDto from(YouthMemberInfo youthMemberInfo) {
        return YouthMemberInfoResponseDto.builder()
                .wakeUpTime(youthMemberInfo.getWakeUpTime())
                .sleepTime(youthMemberInfo.getSleepTime())
                .breakfast(youthMemberInfo.getBreakfast())
                .lunch(youthMemberInfo.getLunch())
                .dinner(youthMemberInfo.getDinner())
                .outgoingTime(youthMemberInfo.getOutgoingTime())
                .wakeUpAlarm(youthMemberInfo.isWakeUpAlarm())
                .sleepAlarm(youthMemberInfo.isSleepAlarm())
                .breakfastAlarm(youthMemberInfo.isBreakfastAlarm())
                .lunchAlarm(youthMemberInfo.isLunchAlarm())
                .dinnerAlarm(youthMemberInfo.isDinnerAlarm())
                .outgoingAlarm(youthMemberInfo.isOutgoingAlarm())
                .build();
    }
}
