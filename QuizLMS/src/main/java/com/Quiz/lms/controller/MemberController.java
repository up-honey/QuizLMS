package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.register(memberDTO);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO) {
        return memberService.login(memberDTO.getUsername(), memberDTO.getPassword())
                .map(member -> ResponseEntity.ok("Login successful"))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 로그아웃 기능 구현 (토큰 제거 등)
        return ResponseEntity.ok("Logout successful");
    }
}
