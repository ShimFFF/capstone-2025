package naeilmolae.domain.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScriptValidationResponseDto {
    @JsonProperty("is_proper") // JSON 키와 매핑
    boolean isProper;
    Integer reason;
}
