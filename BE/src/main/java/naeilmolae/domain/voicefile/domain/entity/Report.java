package naeilmolae.domain.voicefile.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseEntity;

// 공통 필드만 모아둔 추상 클래스
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 추후에 member와의 관계를 제거하기 위해 Long으로 전환해야함.
    // 신고한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 신고 사유
    @Column(nullable = false, length = 500)
    private String reason;

    protected Report(Member member, String reason) {
        this.member = member;
        this.reason = reason;
    }
}