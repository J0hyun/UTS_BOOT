package com.mbc.controller;

import com.mbc.dto.MemberFormDto;
import com.mbc.entity.Member;
import com.mbc.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
                             BindingResult bindingResult, Model model, @RequestParam("profileImg") MultipartFile profileImgFile) throws Exception
    {
        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try{
            memberService.saveMember(memberFormdto, profileImgFile);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login") // 로그인 페이지
    public String login(HttpServletRequest request) {
        // 이미 로그인한 경우, 로그인 페이지로 접근하지 못하게 막고 홈 페이지로 리다이렉트
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // 이미 로그인한 경우 홈 페이지로 리다이렉트
        }

        return "/member/memberLoginForm"; // 로그인 페이지로 이동
    }

    @GetMapping(value = "/login/error") // 로그인 실패 시 처리
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요");
        return "member/memberLoginForm";
    }

}
