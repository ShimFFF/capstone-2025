package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.*;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.request.WithdrawalReasonRequest;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberNumResponseDto;
import naeilmolae.domain.member.dto.response.YouthMemberInfoResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.HelperMemberInfoRepository;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.repository.MemberWithdrawalReasonRepository;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
import naeilmolae.domain.member.status.MemberErrorStatus;
import naeilmolae.global.common.exception.RestApiException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final YouthMemberInfoRepository youthMemberInfoRepository;
    private final HelperMemberInfoRepository helperMemberInfoRepository;
    private final MemberWithdrawalReasonRepository memberWithdrawalReasonRepository;

    private final MemberRefreshTokenService refreshTokenService;

    @Override
    public Member findById(Long id) throws UsernameNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.EMPTY_MEMBER));
    }

    // 회원 저장
    @Override
    @Transactional
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    // 회원가입 함수 (멤버 기본 정보 등록)
    @Override
    @Transactional
    public MemberIdResponseDto signUpInfo(Member member, MemberInfoRequestDto request) {
        Member findMember = findById(member.getId());

        if (request.role().equals(Role.HELPER)) {
            //나이가 성인이 아니면 예외처리 (만 19세가 아닌 성인이 기준)
            // 현재 연도 - 태어난 연도 < 19 이면 예외처리
            if (LocalDateTime.now().getYear() - request.birth().getYear() < 19) {
                throw new RestApiException(MemberErrorStatus.INVALID_HELPER_AGES);
            }

            // 헬퍼 멤버 INFO 업데이트
            HelperMemberInfo helperMemberInfo = helperMemberInfoRepository.save(new HelperMemberInfo(true, true));
            findMember.registerHelperInfo(helperMemberInfo);
        }

        // 기본 정보 업데이트
        updateMemberBasicInfo(findMember, request);

        return new MemberIdResponseDto(findMember.getId());
    }

    // 회원가입 함수 (청년 위치 정보 등록)
    @Override
    @Transactional
    public MemberIdResponseDto signUpYouth(Member member, YouthMemberInfoDto request) {
        Member findMember = findById(member.getId());
        // 기본 정보 업데이트
        handleRoleSpecificInfo(findMember, request);

        return new MemberIdResponseDto(saveEntity(findMember).getId());
    }

    // 회원 탈퇴 함수
    @Override
    @Transactional
    public MemberIdResponseDto withdrawal(Member member, WithdrawalReasonRequest request) {
        // 멤버 soft delete
        Member loginMember = findById(member.getId());

        // refreshToken 삭제. 알림 안 가게 하기 위함. 청년/조력자 모두 해당
        refreshTokenService.deleteRefreshToken(loginMember);

        // 멤버 정보 삭제
        loginMember.setYouthMemberInfo(null);
        loginMember.setHelperMemberInfo(null);
        loginMember.clearSensitiveInfo();

        // 멤버 soft delete 하기
         loginMember.delete();

        // 탈퇴 사유 저장
        request.reasonList()
                .forEach(reason -> memberWithdrawalReasonRepository.save(new MemberWithdrawalReason(reason)));

        return new MemberIdResponseDto(loginMember.getId());
    }

    //회원 정보 수정
    @Override
    @Transactional
    public MemberIdResponseDto updateMemberInfo(Member member, MemberInfoRequestDto request) {
        Member loginMember = findById(member.getId());

        // 기본 정보 업데이트
        updateMemberBasicInfo(loginMember, request);

        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    //청년 회원 정보 수정
    @Override
    @Transactional
    public MemberIdResponseDto updateYouthMemberInfo(Member member, YouthMemberInfoUpdateDto request) {
        Member loginMember = findById(member.getId());
        YouthMemberInfo youthMemberInfo = loginMember.getYouthMemberInfo();

        if (youthMemberInfo == null || !loginMember.getRole().equals(Role.YOUTH)) {
            throw new RestApiException(MemberErrorStatus.NOT_YOUTH);
        }

        // 청년 정보 업데이트
        youthMemberInfo.updateYouthMemberInfoDto(request);

        member.setYouthMemberInfo(youthMemberInfo);
        youthMemberInfoRepository.save(youthMemberInfo);

        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    @Override
    public YouthMemberInfoResponseDto getYouthMemberInfo(Member member) {
        Member loginMember = findById(member.getId());
        YouthMemberInfo youthMemberInfo = loginMember.getYouthMemberInfo();

        if (youthMemberInfo == null) {
            throw new RestApiException(MemberErrorStatus.NOT_YOUTH);
        }
        return YouthMemberInfoResponseDto.from(youthMemberInfo);
    }

    @Override
    public HelperMemberInfo getHelperMemberInfo(Member member) {
        Member findMember = memberRepository.findByIdAndHasHelperInfo(member.getId())
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_HELPER));

        return findMember.getHelperMemberInfo();
    }

    // 기본 정보 업데이트
    private void updateMemberBasicInfo(Member member, MemberInfoRequestDto request) {
        member.updateMemberInfo(request);
    }

    // 역할에 따라 추가 정보 처리
    @Transactional
    public void handleRoleSpecificInfo(Member member, YouthMemberInfoDto request) {
        member.changeRole(Role.YOUTH);
        if (member.getRole().equals(Role.YOUTH)) {
            // 청년 정보 처리
            YouthMemberInfo youthMemberInfo = member.getYouthMemberInfo();
            if (youthMemberInfo == null) {
                // 청년 정보가 없으면 새로 저장
                youthMemberInfo = MemberMapper.toYouthMemberInfo(request);

                member.setYouthMemberInfo(youthMemberInfo);

                youthMemberInfoRepository.save(youthMemberInfo);
            } else {
                // 청년 정보가 있으면 업데이트
                youthMemberInfo.updateYouthMemberInfoDto(request);
            }
        }
    }

    public MemberInfoResponseDto getMemberInfo(Member member) {
        Member loginMember = findById(member.getId());
        // loginMember.getYouthMemberInfo()가 존재하면 함께 반환
        return loginMember.getYouthMemberInfo() != null
                ? MemberMapper.toMemberInfoResponseDto(
                loginMember,
                MemberMapper.toYouthMemberInfoDto(loginMember.getYouthMemberInfo())
        )
                : MemberMapper.toMemberInfoResponseDto(loginMember);
    }

    @Override
    public MemberNumResponseDto getMemberNum(Role role) {
        return new MemberNumResponseDto(memberRepository.countAllByRole(role));
    }

    @Override
    public List<Member> getAllYouthMember() {
        return memberRepository.findAllYouthMembersWithInfo(Role.YOUTH);
    }

    @Override
    public List<Member> getAllHelperMember() {
        return memberRepository.findAllHelperMembersWithInfo(Role.HELPER);
    }

    @Override
    public LocalDateTime getLastLoginDate(Member member) {
        return refreshTokenService.getLastIssueAt(member.getRefreshToken());
    }
}
