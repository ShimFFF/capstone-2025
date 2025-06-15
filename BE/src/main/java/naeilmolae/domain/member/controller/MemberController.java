package naeilmolae.domain.member.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.member.domain.HelperMemberInfo;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.request.WithdrawalReasonRequest;
import naeilmolae.domain.member.dto.response.*;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import naeilmolae.global.util.S3FileComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final S3FileComponent s3FileComponent;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Tag(name = "회원 가입")
    @Operation(summary = "회원가입 API (멤버 기본 정보 등록)", description = "최초 멤버 정보를 등록하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PostMapping
    public BaseResponse<MemberIdResponseDto> signUpInfo(@CurrentMember Member member,
                                                    @Valid @RequestBody MemberInfoRequestDto request) {
        log.info("signUpInfo request: {}", request);
        log.info("member: {}", member);
        return BaseResponse.onSuccess(memberService.signUpInfo(member, request));
    }

    @Tag(name = "회원 가입")
    @Operation(summary = "회원가입 API (청년 위치, 알림 시간 정보 등록)", description = "최초 멤버 정보를 등록하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PostMapping("/youth")
    public BaseResponse<MemberIdResponseDto> signUpLocation(@CurrentMember Member member,
                                                    @Valid @RequestBody YouthMemberInfoDto request) {
        return BaseResponse.onSuccess(memberService.signUpYouth(member, request));
    }

    @Tag(name = "회원 탈퇴")
    @Operation(summary = "회원 탈퇴 API", description = "해당 유저 정보를 삭제하는 API입니다. 회원 탈퇴 사유는 그냥 String 값으로 주시면 됩니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping
    public BaseResponse<MemberIdResponseDto> withdrawal(@CurrentMember Member member,
                                                        @RequestBody WithdrawalReasonRequest request) {
        return BaseResponse.onSuccess(memberService.withdrawal(member, request));
    }

    @Tag(name = "정보 수정")
    @Operation(summary = "회원 정보 수정 API", description = "멤버 정보 수정하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PatchMapping("/info")
    public BaseResponse<MemberIdResponseDto> patchCommonInfo(@CurrentMember Member member,
                                                            @Valid @RequestBody MemberInfoRequestDto request) {
        return BaseResponse.onSuccess(memberService.updateMemberInfo(member, request));
    }

    @Tag(name = "정보 수정")
    @Operation(summary = "청년 회원 정보 수정 (위치, 알림 시간) API", description = "청년 멤버 정보 수정하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PatchMapping("/info/youth")
    public BaseResponse<MemberIdResponseDto> patchYouthInfo(@CurrentMember Member member,
                                                            @Valid @RequestBody YouthMemberInfoUpdateDto request) {
        return BaseResponse.onSuccess(memberService.updateYouthMemberInfo(member, request));
    }

    @Tag(name = "회원 정보 조회")
    @Operation(summary = "청년 회원 정보 조회 API", description = "청년 멤버 정보 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/info/youth")
    public BaseResponse<YouthMemberInfoResponseDto> getYouthInfo(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getYouthMemberInfo(member));
    }

    @Tag(name = "회원 정보 조회")
    @Operation(summary = "봉사자 회원 정보 조회 API", description = "봉사자 멤버 정보 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/info/helper")
    public BaseResponse<HelperMemberInfoResponseDto> getHelperInfo(@CurrentMember Member member) {
        HelperMemberInfo helperMemberInfo = memberService.getHelperMemberInfo(member);
        return BaseResponse.onSuccess(HelperMemberInfoResponseDto.of(helperMemberInfo));
    }


    @Tag(name = "회원 정보 조회")
    @Operation(summary = "회원 정보 조회 API", description = "멤버 정보 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping
    public BaseResponse<MemberInfoResponseDto> getMemberIfo(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberInfo(member));
    }

    @Tag(name = "회원 정보 조회")
    @Operation(summary = "청년 회원 수 조회 API(메인)", description = "청년 회원 수 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/youth-num")
    public BaseResponse<MemberNumResponseDto> getYouthMemberNum(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberNum(Role.YOUTH));
    }

    @Tag(name = "회원 정보 조회")
    @Operation(summary = "조력자 회원 수 조회 API(메인)", description = "조력자 회원 수 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/helper-num")
    public BaseResponse<MemberNumResponseDto> getHelperMemberNum(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberNum(Role.HELPER));
    }

    @Tag(name = "정보 수정")
    @Operation(summary = "프로필 사진 수정 endpoint 조회", description = "사용자 이미지 저장 및 수정을 위한 endpoint를 반환합니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/presigned-url")
    public BaseResponse<Map<String, String>> getPresignedUrl(
            @RequestParam String fileName,
            @RequestParam String contentType) {

        URL presignedUrl = s3FileComponent.generatePresignedUrl(fileName, contentType);

        Map<String, String> response = new HashMap<>();
        response.put("uploadUrl", presignedUrl.toString());
        response.put("fileUrl", "https://" + bucketName + ".s3.amazonaws.com/" + fileName);

        return BaseResponse.onSuccess(response);
    }


}
