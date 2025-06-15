package naeilmolae.domain.alarm.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.entity.CategoryType;
import naeilmolae.domain.alarm.application.dto.response.AlarmCategoryCountResponse;
import naeilmolae.domain.voicefile.application.usecase.ProvidedFileAdapterUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmViewUseCase {
    private final AlarmUseCase alarmUsecase;

    private final ProvidedFileAdapterUseCase providedFileAdapterUseCase;
    private final AlarmCategoryUseCase alarmCategoryUseCase;


    // 알림 조회
    public AlarmResponse findRecommendedAlarm(Long memberId, String childAlarmCategory) {
        AlarmCategory alarmCategory = alarmCategoryUseCase.findByString(childAlarmCategory);
        return alarmUsecase.findByAlarmCategory(alarmCategory);
    }


    // 이번 주에 사용된 알람의 부모 카테고리 조회
    public Set<AlarmCategory> findDistinctCreatedCategories(Long memberId) {
        // 이번주에 사용된 AlarmId 조회
        Set<Long> alarmIds
                = providedFileAdapterUseCase.findAlarmIdsProvidedThisWeek(memberId);
        return alarmUsecase.findByIdIn(alarmIds)
                .stream()
                .map(AlarmResponse::getAlarmCategory)
                .map(alarmCategoryUseCase::findByString)
                .map(AlarmCategory::getParent)
                .collect(Collectors.toSet());
    }

    // 카테고리 순서 맞추기
    public List<AlarmCategoryCountResponse> findUserCategoryCount(Long memberId, CategoryType categoryType) {
        Set<AlarmCategory> userUsed = this.findDistinctCreatedCategories(memberId)
                .stream()
                .filter(item -> item.getCategoryType().equals(categoryType))
                .collect(Collectors.toSet());

        List<AlarmCategoryCountResponse> collect = AlarmCategory.ROOT_CATEGORIES
                .stream()
                .filter(item -> item.getCategoryType().equals(categoryType))
                .map(AlarmCategoryCountResponse::new)
                .collect(Collectors.toList());

        // collect 리스트를 순회하면서 userUsed에 포함된 요소는 맨 뒤로 이동
        List<AlarmCategoryCountResponse> sortedCollect = new ArrayList<>();

        // userUsed에 포함되지 않은 항목 먼저 추가
        collect.stream()
                .filter(item -> !userUsed.contains(item.getAlarmCategory()))
                .forEach(sortedCollect::add);

        // userUsed에 포함된 항목을 뒤에 추가
        collect.stream()
                .filter(item -> userUsed.contains(item.getAlarmCategory()))
                .forEach(item -> {
                    item.setUsed();         // isUsed 값을 true로 설정
                    sortedCollect.add(item); // sortedCollect에 추가
                });

        return sortedCollect;
    }
}
