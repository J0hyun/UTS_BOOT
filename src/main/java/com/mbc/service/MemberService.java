package com.mbc.service;

import com.mbc.entity.Member;
import com.mbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);   // INSERT 또는 UPDATE SQL 자동 생성
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다");
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

    public String getStoreMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        log.info(member);

        return member.getName();
    }
}
