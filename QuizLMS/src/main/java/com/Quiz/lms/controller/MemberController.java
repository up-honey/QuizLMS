package com.Quiz.lms.controller;

import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDTO memberDTO) {
        try {
            memberService.register(memberDTO);
            return ResponseEntity.ok("Registration successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO) {
        return memberService.login(memberDTO.getUsername(), memberDTO.getPassword())
                .map(member -> ResponseEntity.ok("Login successful"))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 로그아웃 기능 구현 (예: 세션 무효화, 토큰 제거 등)
        return ResponseEntity.ok("Logout successful");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
    
    //check 0906 중복 검사
    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean isTaken = memberRepository.findByUsername(username).isPresent();
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        boolean isTaken = memberRepository.findByEmail(email).isPresent();
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam String nickname) {
        boolean isTaken = memberRepository.findByNickname(nickname).isPresent();
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-phone-number")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkPhoneNumber(@RequestParam String phoneNumber) {
        boolean isTaken = memberRepository.findByPhoneNumber(phoneNumber).isPresent();
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        return ResponseEntity.ok(response);
    }



}

