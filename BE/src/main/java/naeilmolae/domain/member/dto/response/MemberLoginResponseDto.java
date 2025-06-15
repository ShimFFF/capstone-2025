package naeilmolae.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import naeilmolae.domain.member.domain.Role;

@Getter
@Builder
@Schema(description = "소셜 로그인 성공 응답 객체")
public class MemberLoginResponseDto {

    @Schema(description = "회원 고유 ID", example = "12345")
    private Long memberId;

    @Schema(description = "회원 닉네임", example = "나일몰래")
    private String nickname;

    @Schema(description = "로그인 시 발급되는 JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String accessToken;

    @Schema(description = "로그인 시 발급되는 JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;

    @Schema(description = "서비스 회원 여부 (true: 서비스에 등록된 회원, false: 비회원, 비회원시 회원가입 진행)", example = "true")
    private boolean isServiceMember;

    @Schema(description = "서비스 역할 (YOUTH: 청년, HELPER: 조력자, GUEST: (회원가입 중 or 비회원 로그인)", example = "YOUTH")
    private Role role;

    @Schema(description = "기본 정보 등록 상태", example = "true")
    private boolean infoRegistered;

    @Schema(description = "위치 정보 등록 상태", example = "true")
    private boolean locationRegistered;

    @Schema(description = "푸시 알림 시간 정보 등록 상태", example = "true")
    private boolean pushTimeRegistered;
}

