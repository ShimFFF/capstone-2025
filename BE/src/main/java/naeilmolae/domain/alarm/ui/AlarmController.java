package naeilmolae.domain.alarm.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.dto.response.AlarmCategoryCountResponse;
import naeilmolae.domain.alarm.application.dto.response.AlarmCategoryMessageResponse;
import naeilmolae.domain.alarm.application.dto.response.AlarmCategoryWithMessageResponse;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import naeilmolae.domain.alarm.application.dto.response.CategoryTypeResponse;
import naeilmolae.domain.alarm.application.usecase.AlarmCategoryUseCase;
import naeilmolae.domain.alarm.application.usecase.AlarmViewUseCase;
import naeilmolae.domain.alarm.domain.entity.AlarmCategory;
import naeilmolae.domain.alarm.domain.entity.CategoryType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static naeilmolae.domain.alarm.domain.entity.AlarmCategory.ROOT_CATEGORIES;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmViewUseCase alarmViewUseCase;
    private final AlarmCategoryUseCase alarmCategoryUseCase;

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 0단계: CategoryType 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/category-type")
    public BaseResponse<List<CategoryTypeResponse>> getCategoryTypeList() {
        return BaseResponse.onSuccess(List.of(CategoryType.values())
                .stream()
                .map(CategoryTypeResponse::new)
                .toList());
    }

    // valid
    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 위로 청취 1단계: 위로 목록 조회 -> '[Common] ChildrenCategoryId로 AlarmId 조회'로 이동", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/comfort")
    public BaseResponse<List<AlarmCategoryWithMessageResponse>> getComfortList(@CurrentMember Member member) {
        List<AlarmCategory> list = ROOT_CATEGORIES
                .stream()
                .filter(item -> item.getCategoryType().equals(CategoryType.COMFORT))
                .collect(Collectors.toList());

        List<AlarmCategoryWithMessageResponse> collect = list
                .stream()
                .map(AlarmCategoryWithMessageResponse::new)
                .collect(Collectors.toList());


        collect.stream()
                .forEach(item -> {
                    AlarmCategory.getChildrenByParent(item.getAlarmCategory())
                            .stream()
                            .map(AlarmCategoryWithMessageResponse::new)
                            .forEach(item::addChildren);
                });
        return BaseResponse.onSuccess(collect);
    }


    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [Common] ChildrenCategoryId로 AlarmId 조회 / [봉사자] 녹음 2단계 & [청년] 위로 청취 2단계", description = "위로 녹음9 에서 상단에 보여줄 멘트를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/alarm-category/{childrenAlarmCategory}/detail")
    public BaseResponse<AlarmCategoryMessageResponse> getAlarmCategory(@CurrentMember Member member,
                                                                       @PathVariable(value = "childrenAlarmCategory") String childrenAlarmCategory) {
        AlarmResponse recommendedAlarm = alarmViewUseCase.findRecommendedAlarm(member.getId(), childrenAlarmCategory);
        AlarmCategory alarmCategory = alarmCategoryUseCase.findByString(childrenAlarmCategory);
        AlarmCategoryMessageResponse alarmCategoryMessageResponseDto = new AlarmCategoryMessageResponse(recommendedAlarm.getAlarmId(),
                recommendedAlarm.getAlarmCategory(),
                alarmCategory.getTitle());
        return BaseResponse.onSuccess(alarmCategoryMessageResponseDto);
    }

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 1단계: 녹음 선택 리스트 조회 -> '[Common] ChildrenCategoryId로 AlarmId 조회'로 이동", description = "사용자의 녹음할 리스트를 조회합니다. 정렬은 1. 사용자가 작성하지 않음 2. 데이터 수가 적음 순으로 보여지게 됩니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/list/{categoryType}")
    public BaseResponse<List<AlarmCategoryCountResponse>> getAlarmList(@CurrentMember Member member,
                                                                       @PathVariable CategoryType categoryType) {
        List<AlarmCategoryCountResponse> collect =
                alarmViewUseCase.findUserCategoryCount(member.getId(), categoryType);

        // isUsed == false (미작성) 먼저
        collect.sort(Comparator.comparing(AlarmCategoryCountResponse::isUsed));

        return BaseResponse.onSuccess(collect);
    }


    @Tag(name = "[봉사자] 메인 페이지")
    @Operation(summary = "[VALID] [봉사자] 동기부여 4단계: 카테고리 목록 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/")
    public BaseResponse<List<AlarmCategoryWithMessageResponse>> getAlarmCategoryList(@CurrentMember Member member) {
        List<AlarmCategoryWithMessageResponse> collect = ROOT_CATEGORIES
                .stream()
                .map(AlarmCategoryWithMessageResponse::new)
                .collect(Collectors.toList());
//        List<AlarmCategoryWithMessageResponseDto> list = alarmCategoryMessageUseCase.findByAlarmCategoryIn(ROOT_CATEGORIES)
//                .stream()
//                .map(alarmCategoryMessage -> new AlarmCategoryWithMessageResponseDto(alarmCategoryMessage.getAlarmCategory(), alarmCategoryMessage))
//                .collect(Collectors.toList());
        return BaseResponse.onSuccess(collect);
    }
}
