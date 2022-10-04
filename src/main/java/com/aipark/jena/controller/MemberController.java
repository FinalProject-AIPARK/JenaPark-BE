package com.aipark.jena.controller;

import com.aipark.jena.dto.Response.Body;
import com.aipark.jena.dto.ResponseMember;
import com.aipark.jena.service.MemberService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.aipark.jena.dto.RequestMember.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Body> signUp(@Valid @RequestBody SignUp memberDto, Errors errors) {
        if(errors.hasErrors()){

        }
        return memberService.signUp(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Body> login(@RequestBody Login loginDto) {
        return memberService.login(loginDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Body> reissue(@RequestBody Reissue reissueDto) {
        return memberService.reissue(reissueDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Body> logout(@RequestBody Logout logoutDto) {
        return memberService.logout(logoutDto);
    }

    @GetMapping
    public ResponseEntity<Body> getInfo() {
        return memberService.getInfo();
    }
}
