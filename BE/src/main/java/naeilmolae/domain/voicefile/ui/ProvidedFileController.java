package naeilmolae.domain.voicefile.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import naeilmolae.domain.alarm.application.usecase.AlarmAdapterUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.SimpleMemberDto;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.voicefile.application.dto.request.ReportRequestDto;
import naeilmolae.domain.voicefile.application.usecase.ReportUseCase;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.application.dto.request.ThanksMessageRequestDto;
import naeilmolae.domain.voicefile.application.dto.response.ProvidedFileResponseDto;
import naeilmolae.domain.voicefile.application.dto.response.VoiceFileReactionSummaryResponseDto;
import naeilmolae.domain.voicefile.application.usecase.ProvidedFileUseCase;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/providedfile")
public class ProvidedFileController {
    private final ProvidedFileUseCase providedFileUseCase;

    private final AlarmAdapterUseCase alarmAdapterUsecase;
    private final MemberAdapterService memberAdapterService;
    private final ReportUseCase reportUseCase;

    @Tag(name = "[봉사자] 메인 페이지")
    @Operation(summary = "[봉사자] 동기부여 2단계: 자신의 녹음에 대해 사람들이 남긴 정보 요약 API", description = "동기부여 메인 페이지 내용, 자신의 녹음에 대해 사람들이 남긴 정보 요약")
    @GetMapping("/summary")
    public BaseResponse<VoiceFileReactionSummaryResponseDto> getTotalReaction(@CurrentMember Member member) {
        return BaseResponse.onSuccess(providedFileUseCase.getTotalReaction(member.getId()));
    }

    @Tag(name = "[봉사자] 메인 페이지")
    @Operation(summary = "[VALID] [봉사자] 동기부여 3단계: 청년의 편지 조회", description = "동기부여0 페이지 내용, 청년의 편지를 조회합니다.")
    @GetMapping("/list")
    public BaseResponse<Page<ProvidedFileResponseDto>> getProvidedFileList(@CurrentMember Member member,
                                                                           @RequestParam(value = "parentCategory", required = false) String parentCategory,
                                                                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProvidedFile> providedFiles =
                providedFileUseCase.getProvidedFiles(member.getId(), parentCategory, pageable);

        // Step 1: ProvidedFile의 voiceFile의 alarmId를 추출
        Set<Long> alarmIds = providedFiles.stream()
                .map(providedFile -> providedFile.getVoiceFile().getAlarmId())
                .collect(Collectors.toSet());

        // Step 2: AlarmId를 사용하여 AlarmResponseDto Map 생성
        Map<Long, AlarmResponse> alarms = alarmAdapterUsecase.findAlarmsByIds(alarmIds);

        // Step 3: 청년 맴버 조회
        List<Long> ids = providedFiles.stream().map(ProvidedFile::getConsumerId).collect(Collectors.toList());
        Map<Long, SimpleMemberDto> memberInfoMap = memberAdapterService.getSimpleMemberDtoMapByIds(ids);

        // Step 4: Page의 T를 ProvidedFileResponseDto로 변환
        Page<ProvidedFileResponseDto> responseDtos = providedFiles.map(providedFile -> {
            AlarmResponse alarm = alarms.get(providedFile.getVoiceFile().getAlarmId());
            return ProvidedFileResponseDto.from(providedFile, alarm.getAlarmCategoryKo(), memberInfoMap.get(providedFile.getConsumerId()));
        });

        return BaseResponse.onSuccess(responseDtos);
    }

    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 청취 3-1단계: 감사 메시지 보내기", description = "청년이 봉사자에게 감사 메시지를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @PostMapping("/{providedFileId}/comment")
    public BaseResponse<List<String>> likeProvidedFile(@CurrentMember Member member,
                                                  @PathVariable Long providedFileId,
                                                  @RequestBody ThanksMessageRequestDto requestDto) {
        List<String> strings = providedFileUseCase.likeProvidedFile(member.getId(), providedFileId, requestDto.message());
        return BaseResponse.onSuccess(strings);
    }

    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 청취 3-2단계: 감사 메시지 지우기", description = "청년이 봉사자에게 감사 메시지를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @DeleteMapping("/{providedFileId}/comment")
    public BaseResponse<List<String>> deleteLikeProvidedFile(@CurrentMember Member member,
                                                       @PathVariable Long providedFileId,
                                                       @RequestBody ThanksMessageRequestDto requestDto) {
        List<String> strings = providedFileUseCase.deleteLikeProvidedFile(member.getId(), providedFileId, requestDto.message());
        return BaseResponse.onSuccess(strings);
    }

    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 청취 4단계: 북마크", description = "청년이 봉사자의 음성을 북마크합니다.")
    @PostMapping("/{providedFileId}/bookmark")
    public BaseResponse<Boolean> bookmarkProvidedFile(@CurrentMember Member member,
                                                      @PathVariable Long providedFileId) {
        providedFileUseCase.bookmarkProvidedFile(member.getId(), providedFileId);
        return BaseResponse.onSuccess(providedFileUseCase.bookmarkProvidedFile(member.getId(), providedFileId));
    }

    @Tag(name = "[봉사자] 메인 페이지")
    @Operation(summary = "감사 메시지 신고", description = "감사 메시지에 부적절한 내용이 있을 경우 신고합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @PostMapping("/{providedFileId}/report")
    public BaseResponse<Boolean> likeProvidedFile(@CurrentMember Member member,
                                                  @PathVariable Long providedFileId,
                                                  @RequestBody ReportRequestDto requestDto) {
        return BaseResponse.onSuccess(reportUseCase.reportProvidedFile(member.getId(), providedFileId, requestDto.reason()));
    }

    @Tag(name = "[봉사자] 메인 페이지")
    @Operation(summary = "감사 메시지 삭제", description = "감사 메시지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @DeleteMapping("/{providedFileId}")
    public BaseResponse<Boolean> deleteProvidedFile(@CurrentMember Member member,
                                                    @PathVariable Long providedFileId,
                                                    @RequestBody ReportRequestDto requestDto) {
        return BaseResponse.onSuccess(reportUseCase.reportProvidedFile(member.getId(), providedFileId, requestDto.reason()));
    }
}
