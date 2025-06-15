package naeilmolae.domain.alarm.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.application.usecase.AlarmSettingService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm-setting")
public class AlarmSettingController {

    private final AlarmSettingService alarmSettingService;

    @Tag(name = "정보 수정")
    @Operation(summary = "청년 알림 설정 수정 API", description = "청년 알림 설정 수정 API")
    @PostMapping("/toggle/{alarmCategory}/{bool}")
    public void toggleAlarmSetting(@CurrentMember Member member, @PathVariable AlarmCategory alarmCategory, @PathVariable boolean bool) {
        alarmSettingService.updateAlarm(member, alarmCategory, bool);
    }

    @Tag(name = "정보 수정")
    @Operation(summary = "봉사자 알림 설정 수정 API", description = "봉사자 알림 설정 수정 API")
    @PostMapping("/toggle/helper/{notificationType}/{bool}")
    public void toggleAlarmSetting(@CurrentMember Member member, @PathVariable NotificationType notificationType, @PathVariable boolean bool) {
        alarmSettingService.updateHelperAlarm(member, notificationType, bool);
    }

}
