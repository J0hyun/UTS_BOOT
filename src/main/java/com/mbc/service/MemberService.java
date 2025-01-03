package com.mbc.service;

import com.mbc.dto.MemberFormDto;
import com.mbc.dto.MemberImgDto;
import com.mbc.entity.Member;
import com.mbc.entity.MemberImg;
import com.mbc.repository.MemberImgRepository;
import com.mbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final MemberImgService memberImgService;
    private final PasswordEncoder passwordEncoder;

    public Long saveMember(MemberFormDto memberFormDto, MultipartFile profileImgFile) throws Exception {
        Member member = Member.createMember(memberFormDto, passwordEncoder) ;
        validateDuplicateMember(member);
        memberRepository.save(member);

        // 프로필 이미지 저장
        if (profileImgFile != null && !profileImgFile.isEmpty()) {
            MemberImg memberImg = new MemberImg();
            memberImg.setMember(member);
            memberImgService.saveMemberImg(memberImg, profileImgFile);
        }

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        Member findMember2 = memberRepository.findByname(member.getName());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다");
        }
        if (findMember2 != null) {
            throw new IllegalStateException("이미 가입된 아이디입니다");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        log.info("------loadUserByUsername 진입확인------");
        Member member = memberRepository.findByname(name);

        if(member == null) {
            throw new UsernameNotFoundException(name);
        }

        return User.builder()
                .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

//    public Member getStoreMember(Long id) {
//        Member member = memberRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("회원이 존재하지 않습니다."));
//
//        log.info(member);
//
//        return member;
//    }

    public MemberFormDto getStoreMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 프로필 이미지 가져오기
        MemberImg memberImg = memberImgRepository.findByMemberId(id);
        MemberFormDto memberFormDto;
        if (memberImg != null) {
            MemberImgDto memberImgDto = MemberImgDto.of(memberImg);
            // MemberFormDto 생성 (Builder 사용)
            memberFormDto = MemberFormDto.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .phone(member.getPhone())
                    .regTime(member.getRegTime())
                    .viewPofile(memberImgDto.getImgUrl())
                    .build();
        }else {
            // MemberFormDto 생성 (Builder 사용)
            memberFormDto = MemberFormDto.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .phone(member.getPhone())
                    .regTime(member.getRegTime())
                    .build();
        }
        return memberFormDto;
    }

    public MemberFormDto getMemberByUserName(String userName) {
        Member member = memberRepository.findByname(userName); // memberRepository에서 이름으로 찾기

        Long memberId = member.getId();
        // 프로필 이미지 가져오기
        MemberImg memberImg = memberImgRepository.findByMemberId(memberId);
        MemberFormDto memberFormDto;
        if (memberImg != null) {
            MemberImgDto memberImgDto = MemberImgDto.of(memberImg);
            // MemberFormDto 생성 (Builder 사용)
            memberFormDto = MemberFormDto.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .phone(member.getPhone())
                    .regTime(member.getRegTime())
                    .viewPofile(memberImgDto.getImgUrl())
                    .build();
        }else {
            // MemberFormDto 생성 (Builder 사용)
            memberFormDto = MemberFormDto.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .phone(member.getPhone())
                    .regTime(member.getRegTime())
                    .build();
        }
        return memberFormDto;
    }
}
