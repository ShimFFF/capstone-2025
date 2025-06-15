package naeilmolae.domain.member.dto.request;

import java.util.List;

public record WithdrawalReasonRequest(
    List<String> reasonList
) {
}
