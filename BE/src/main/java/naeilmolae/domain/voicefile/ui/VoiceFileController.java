package naeilmolae.domain.voicefile.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.usecase.AlarmAdapterUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.SimpleMemberDto;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.voicefile.application.dto.request.ReportRequestDto;
import naeilmolae.domain.voicefile.application.dto.response.*;
import naeilmolae.domain.voicefile.application.usecase.ReportUseCase;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.application.dto.request.UploadContentRequestDto;
import naeilmolae.domain.voicefile.application.usecase.ProvidedFileUseCase;
import naeilmolae.domain.voicefile.application.usecase.VoiceFileUseCase;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import naeilmolae.global.util.S3FileComponent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voicefiles")
public class VoiceFileController {
    private final VoiceFileUseCase voiceFileUseCase;
    private final ProvidedFileUseCase providedFileUseCase;
    private final MemberAdapterService memberAdapterService;
    private final S3FileComponent s3FileComponent;
    private final AlarmAdapterUseCase alarmAdapterUsecase;
    private final ReportUseCase reportUseCase;

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 3-1단계: 스크립트 GPT에게 작성 요청", description = "GPT에게 스크립트 작성을 요청합니다.")
    @PostMapping("/{alarmId}/gpt")
    public BaseResponse<GptGenerationResponseDto> gptContent(@CurrentMember Member member,
                                                             @PathVariable(value = "alarmId") Long alarmId) {
        String content = alarmAdapterUsecase.findAllByAlarmId(alarmId)
                .getContent();
        return BaseResponse.onSuccess(new GptGenerationResponseDto(alarmId, content));
    }

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 3-2단계: 작성한 스크립트 저장", description = "사용자가 작성한 스크립트를 저장합니다. GPT에게 검증을 받으며 부적절한 스크립트 제공시 예외가 발생할 수 있습니다. [NOTICE] 에러 응답은 추후에 추가하겠습니다.")
    @PostMapping("/{alarmId}/self")
    public BaseResponse<VoiceFileMetaResponseDto> uploadContent(@CurrentMember Member member,
                                                                @PathVariable(value = "alarmId") Long alarmId,
                                                                @RequestBody UploadContentRequestDto uploadContentRequestDto) {
        VoiceFile voiceFile = voiceFileUseCase.saveContent(member.getId(),
                alarmId,
                uploadContentRequestDto.content());

        return BaseResponse.onSuccess(VoiceFileMetaResponseDto.fromEntity(voiceFile));
    }

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 4단계: 음성 파일 업로드", description = "사용자가 녹음한 음성 파일을 업로드합니다. 분석 결과를 받기 위해 업로드합니다. 업로드시 바로 분석이 진행됩니다.")
    @PostMapping(value = "/{voiceFileId}", consumes = {"multipart/form-data"})
    public BaseResponse<String> uploadVoiceFile(@CurrentMember Member member,
                                                @PathVariable Long voiceFileId,
                                                @RequestParam("file") MultipartFile multipartFile) {

        voiceFileUseCase.verifyUserFile(member.getId(), voiceFileId);
        // category를 "voice"로 지정하여 파일을 S3에 업로드
        String fileUrl = s3FileComponent.uploadFile("voice", multipartFile);
        voiceFileUseCase.saveVoiceFileUrl(voiceFileId, fileUrl);

        // 성공 시, 파일의 S3 URL을 응답으로 반환
        return BaseResponse.onSuccess(fileUrl);
    }

    @Tag(name = "[봉사자] 녹음하기")
    @Operation(summary = "[VALID] [봉사자] 녹음 5단계: 분석 결과 조회", description = "제공한 파일의 분석 결과를 조회합니다. N초마다 polling하여 분석 결과를 받아옵니다.")
    @PostMapping("/analysis/{voiceFileId}")
    public BaseResponse<String> analysisVoiceFile(@CurrentMember Member member,
                                                  @PathVariable Long voiceFileId) {
        voiceFileUseCase.verifyUserFile(member.getId(), voiceFileId);
        voiceFileUseCase.getAnalysisResultRepository(voiceFileId);
        return BaseResponse.onSuccess("OK");
    }

    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 청취 1단계: 사용 가능한 음성 파일 ID 조회", description = "사용자가 청취할 수 있는 음성 파일 ID를 조회합니다.")
    @GetMapping
    public BaseResponse<AvailableVoiceFileResponseDto> getAvailableDataList(@CurrentMember Member member,
                                                                            @RequestParam("alarm-id") Long alarmId) { // 실제는 childrenCategoryId 임
        VoiceFile voiceFile = voiceFileUseCase.getAvailableDataList(member.getId(), alarmId);
        ProvidedFile save = providedFileUseCase.save(member.getId(), voiceFile.getId());
        SimpleMemberDto memberDto = memberAdapterService.getSimpleMemberDtoByVoiceFileId(voiceFile.getId());
        return BaseResponse.onSuccess(AvailableVoiceFileResponseDto.from(voiceFile, save.getId(), memberDto));
    }

    @Tag(name = "[청년] 음성 듣기")
    @Operation(summary = "[VALID] [청년] 청취 5단계: 음성 파일 신고", description = "청취 중 부적절한 음성을 신고합니다.")
    @PostMapping("/{voiceFileId}/report")
    public BaseResponse<Boolean> reportVoiceFile(@CurrentMember Member member,
                                                 @PathVariable Long voiceFileId,
                                                 @RequestBody ReportRequestDto requestDto) {
        return BaseResponse.onSuccess(reportUseCase.reportVoiceFile(member.getId(), voiceFileId, requestDto.reason()));
    }

    @Tag(name = "회원 가입")
    @Operation(summary = "[청년] 튜토리얼 1단계: 예시 음성 데이터 조회", description = "예시 음성 데이터를 조회합니다.")
    @GetMapping("/example")
    public BaseResponse<VoiceFileResponseDto> getExampleData() { // 실제는 childrenCategoryId 임
        return BaseResponse.onSuccess(new VoiceFileResponseDto(1L, "아침이야! 일어나서 간단한 스트레칭을 하고 아침의 피로를 날려보자!", "https://naeilmolae.s3.ap-northeast-2.amazonaws.com/voice/1_d9a12d65-278b-4df1-8b91-c70b8c25a8c3.wav"));
    }


    @Tag(name = "회원 탈퇴")
    @Operation(summary = "[봉사자] 탈퇴 1단계: 전체 데이터 보여주기", description = "예시 음성 데이터를 조회합니다.")
    @GetMapping("/retention")
    public BaseResponse<RetentionDto> getRetentionData(@CurrentMember Member member) { // 실제는 childrenCategoryId 임
        return BaseResponse.onSuccess(providedFileUseCase.getRetentionData(member.getId()));
    }


}
