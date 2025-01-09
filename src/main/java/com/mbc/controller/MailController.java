package com.mbc.controller;

import com.mbc.dto.MailDTO;
import com.mbc.service.MailService;
import com.mbc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final MemberService memberService;

    @PostMapping("/check/email")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        if (!memberService.emailExist(email)) {
            return new ResponseEntity<>("일치하는 메일이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("이메일을 사용하는 유저가 존재합니다.", HttpStatus.OK);
    }

    @PostMapping("/send/password")
    public ResponseEntity<?> sendPassword(@RequestParam("email") String email) {
        if (!memberService.updatePasswordToken(email)) {
            return new ResponseEntity<>("비밀번호 찾기는 1시간에 한 번 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        String tmpPassword = memberService.getTmpPassword();
        memberService.updatePassword(tmpPassword, email);
        MailDTO mail = mailService.createMail(tmpPassword, email);

        mailService.sendMail(mail);

        return new ResponseEntity<>("비밀번호 발급이 완료되었습니다.", HttpStatus.OK);
    }

    // 아이디 찾기
    @PostMapping("/find/id")
    public ResponseEntity<?> findId(@RequestParam("email") String email) {
        String userId = memberService.findUsernameByEmail(email);
        if (userId == null) {
            return new ResponseEntity<>("이메일에 해당하는 아이디가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("아이디: " + userId, HttpStatus.OK);
    }
}
