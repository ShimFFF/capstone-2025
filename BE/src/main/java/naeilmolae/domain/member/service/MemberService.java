package naeilmolae.domain.member.service;

import naeilmolae.domain.member.domain.HelperMemberInfo;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.request.WithdrawalReasonRequest;
import naeilmolae.domain.member.dto.response.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberService {

    Member findById(Long id) throws UsernameNotFoundException;

    // 회원 저장
    Member saveEntity(Member member);
    // 회원가입
    MemberIdResponseDto signUpInfo(Member member, MemberInfoRequestDto request);
    // 회원가입
    MemberIdResponseDto signUpYouth(Member member, YouthMemberInfoDto request);
    // 회원 탈퇴
    MemberIdResponseDto withdrawal(Member member, WithdrawalReasonRequest request);
    // 회원 정보 수정
    MemberIdResponseDto updateMemberInfo(Member member, MemberInfoRequestDto request);
    // 청년 회원 정보 수정
    MemberIdResponseDto updateYouthMemberInfo(Member member, YouthMemberInfoUpdateDto request);
    // 청년 회원 정보 조회
    YouthMemberInfoResponseDto getYouthMemberInfo(Member member);
    // 봉사자 회원 정보 조회
    HelperMemberInfo getHelperMemberInfo(Member member);
    // 회원 정보 조회
    MemberInfoResponseDto getMemberInfo(Member member);
    // 회원 수 조회
    MemberNumResponseDto getMemberNum(Role role);
    //청년 회원 조회
    List<Member> getAllYouthMember();
    //도우미 회원 조회
    List<Member> getAllHelperMember();
    // 마지막 로그인 날짜 조회
    LocalDateTime getLastLoginDate(Member member);

}