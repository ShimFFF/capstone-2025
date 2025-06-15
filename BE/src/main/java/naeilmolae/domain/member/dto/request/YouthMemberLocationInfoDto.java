package naeilmolae.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "청년회원 정보 객체")
public class YouthMemberLocationInfoDto {
    private Double latitude; // 위도
    private Double longitude; // 경도
}
