package naeilmolae.domain.member.dto.response;

import lombok.Data;
import naeilmolae.domain.member.domain.Member;

@Data
public class SimpleMemberDto {

    private Long id;

    private String name;

    private String profileImage;

    public static SimpleMemberDto from(Member member) {
        SimpleMemberDto simpleMemberDto = new SimpleMemberDto();
        simpleMemberDto.setId(member.getId());
        simpleMemberDto.setName(member.getName());
        simpleMemberDto.setProfileImage(member.getProfileImage());
        return simpleMemberDto;
    }
}
