package naeilmolae.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "멤버 아이디 반환 객체")
public class MemberIdResponseDto {

    @Schema(description = "각 멤버의 고유한 아이디 (Long)", example = "12345")
    private Long memberId;
}
