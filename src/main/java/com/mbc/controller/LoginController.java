package com.mbc.controller;

import com.mbc.dto.MemberFormDto;
import com.mbc.entity.Member;
import com.mbc.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/signup") // 회원가입 페이지
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/signup") // 회원가입 처리
    public String memberForm(@Valid MemberFormDto memberFormdto,
                             BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try{
            Member member = Member.createMember(memberFormdto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login") // 로그인 페이지
    public String login() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error") // 로그인 실패 시 처리
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요");
        return "member/memberLoginForm";
    }

}
