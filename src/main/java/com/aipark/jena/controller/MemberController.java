package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestMember;
import com.aipark.jena.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RequestMember.SignUp memberDto) {
        return memberService.signUp(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestMember.Login loginDto) {
        return memberService.login(loginDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody RequestMember.Reissue reissueDto) {
        return memberService.reissue(reissueDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RequestMember.Logout logoutDto) {
        return memberService.logout(logoutDto);
    }
}
