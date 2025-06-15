package naeilmolae.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberGenerateTokenResponseDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.service.MemberAuthService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "소셜 로그인, 토큰 발급/재발급, 로그아웃 등 인증 관련 작업을 수행하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class MemberAuthController {
    private final MemberAuthService memberAuthService;
    @Operation(summary = "로그인 API", description = "카카오, 애플, 비회원 로그인을 수행하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그인 성공")
    })
    @PostMapping("/login")
    public BaseResponse<MemberLoginResponseDto> socialLogin(@RequestParam(value = "accessToken") String accessToken,
                                                            @RequestParam(value = "loginType") LoginType loginType) {
        log.info(accessToken);
        return BaseResponse.onSuccess(memberAuthService.socialLogin(accessToken, loginType));
    }

    @Operation(summary = "accessToken 재발급 API", description = "refreshToken가 유효하다면 새로운 accessToken을 발급하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH006", description = "유효하지 않는 RefreshToken일 경우 발생")
    })
    @GetMapping("/token/refresh")
    public BaseResponse<MemberGenerateTokenResponseDto> regenerateToken(@RequestHeader(value = "refreshToken") String refreshToken) {
        return BaseResponse.onSuccess(memberAuthService.generateNewAccessToken(refreshToken));
    }

    @Operation(summary = "로그아웃 API", description = "해당 유저의 refreshToken을 삭제하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping("/logout")
    public BaseResponse<MemberIdResponseDto> logout(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberAuthService.logout(member));
    }

}
