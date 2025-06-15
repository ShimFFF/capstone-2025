package naeilmolae.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.global.common.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    private String name;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String clientId;

    private LocalDateTime birth;

    private String deviceId;

    private String fcmToken;

    // TODO 안드로이드 fcm 토큰을 추가해야함.그리고 그에 따른 알림 스케줄링 코드돋 수정해야함
    

    // todo 편의상 DB에 저장, 실제로는 저장하지 않게 해야 함, redis에 저장 추천
    // 지금 refreshToken의 발급 시간을 기준으로 멤버가 얼마나 접속을 안했는 지 판단하는 중
    @Setter
    private String refreshToken;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "youth_member_info_id") // 외래 키 설정
    private YouthMemberInfo youthMemberInfo;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "helper_member_info_id") // 외래 키 설정
    private HelperMemberInfo helperMemberInfo;

    @Builder
    public Member(
            String name, Gender gender, String profileImage
            , Role role, LoginType loginType, String clientId
            , LocalDateTime birth, String deviceId) {
        this.name = name;
        this.gender = gender;
        this.profileImage = profileImage;
        this.role = role;
        this.loginType = loginType;
        this.clientId = clientId;
        this.birth = birth;
        this.deviceId = deviceId;
    }

    // 조력자 정보 등록
    public void registerHelperInfo(HelperMemberInfo helperMemberInfo) {
        this.helperMemberInfo = helperMemberInfo;
    }

    // 널이나 빈 값이 아닌 경우에만 수정
    public void updateMemberInfo(MemberInfoRequestDto request) {
        Optional.ofNullable(request.name()).filter(name -> !name.isEmpty()).ifPresent(name -> this.name = name);
        Optional.ofNullable(request.profileImage()).filter(image -> !image.isEmpty()).ifPresent(image -> this.profileImage = image);
        Optional.ofNullable(request.gender()).ifPresent(gender -> this.gender = gender);
        Optional.ofNullable(request.role()).ifPresent(role -> this.role = role);
        Optional.ofNullable(request.birth()).ifPresent(birth -> this.birth = birth);
        Optional.ofNullable(request.fcmToken()).filter(token -> !token.isEmpty()).ifPresent(token -> this.fcmToken = token);
    }

    public boolean changeRole(Role role) {
        this.role = role;
        return true;
    }

    // 멤버 정보 삭제
    public void clearSensitiveInfo() {
        this.name = null;
        this.profileImage = null;
        this.fcmToken = null;
        this.refreshToken = null;
        this.deviceId = null;
        this.clientId = null;
        this.birth = null;
        this.gender = null;
        this.role = Role.WITHDRAWN; // 예: 탈퇴한 사용자용 ROLE 설정
    }
}
