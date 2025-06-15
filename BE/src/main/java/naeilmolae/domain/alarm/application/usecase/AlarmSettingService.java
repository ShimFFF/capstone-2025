package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.member.domain.HelperMemberInfo;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.repository.HelperMemberInfoRepository;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
import naeilmolae.domain.member.status.MemberErrorStatus;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.global.common.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmSettingService {
    private final YouthMemberInfoRepository youthMemberInfoRepository;
    private final HelperMemberInfoRepository helperMemberInfoRepository;
    private final MemberRepository memberRepository;

    public boolean updateAlarm(Member member, AlarmCategory alarmCategory, boolean alarm) {
//        YouthMemberInfo youthMemberInfo = member.getYouthMemberInfo();
        Member findMember = memberRepository.findByIdAndHasYouthInfo(member.getId())
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_HELPER));
        YouthMemberInfo youthMemberInfo = findMember.getYouthMemberInfo();
        switch (alarmCategory) {
            case WAKE_UP:
                youthMemberInfo.setWakeUpAlarm(alarm);
                break;
            case GO_OUT:
                youthMemberInfo.setOutgoingAlarm(alarm);
                break;
            case MEAL_BREAKFAST:
                youthMemberInfo.setBreakfastAlarm(alarm);
                break;
            case MEAL_LUNCH:
                youthMemberInfo.setLunchAlarm(alarm);
                break;
            case MEAL_DINNER:
                youthMemberInfo.setDinnerAlarm(alarm);
                break;
            case SLEEP:
                youthMemberInfo.setSleepAlarm(alarm);
                break;
            default:
                return false;
        }
        youthMemberInfoRepository.save(youthMemberInfo);
        return true;
    }

    public boolean updateHelperAlarm(Member member, NotificationType notificationType, boolean alarm) {

        if(member.getRole()!= Role.HELPER){
            throw new RestApiException(MemberErrorStatus.NOT_HELPER);
        }

        Member findMember = memberRepository.findByIdAndHasHelperInfo(member.getId())
                .orElseThrow();

        HelperMemberInfo helperMemberInfo = findMember.getHelperMemberInfo();
        if(helperMemberInfo == null){
            throw new RestApiException(MemberErrorStatus.EMPTY_HELPER_INFO);
        }

        switch (notificationType) {
            case THANK_YOU_MESSAGE:
                helperMemberInfo.setThankYouMessage(alarm);
                break;
            case WELCOME_REMINDER:
                helperMemberInfo.setWelcomeReminder(alarm);
                return false;
            default:
                return false;
        }
        helperMemberInfoRepository.save(helperMemberInfo);
        return true;
    }
}
