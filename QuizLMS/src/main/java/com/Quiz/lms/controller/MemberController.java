package com.Quiz.lms.controller;



import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.MemberService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
    
    @GetMapping("/check")
    public Map<String, Object> checkLoginStatus() {
        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            response.put("loggedIn", true);
            response.put("username", authentication.getName()); // 사용자 이름을 반환
        } else {
            response.put("loggedIn", false);
        }

        return response;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDTO memberDTO) {
        log.info("Register request received: {}", memberDTO);  // 요청 로그 남기기
        System.out.println("구동 전");
        try {
            memberService.register(memberDTO);
            log.info("회원가입 성공: {}", memberDTO.getUsername());  // 성공 로그 남기기
            return ResponseEntity.ok("Registration successful");

        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());  // 경고 로그 남기기
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage(), e);  // 에러 로그 남기기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
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

//    @GetMapping("/check-email")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
//        boolean isTaken = memberRepository.findByEmail(email).isPresent();
//        Map<String, Object> response = new HashMap<>();
//        response.put("available", !isTaken);
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping("/check-nickname")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam String nickname) {
//        boolean isTaken = memberRepository.findByNickname(nickname).isPresent();
//        Map<String, Object> response = new HashMap<>();
//        response.put("available", !isTaken);
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping("/check-phone-number")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> checkPhoneNumber(@RequestParam String phoneNumber) {
//        boolean isTaken = memberRepository.findByPhoneNumber(phoneNumber).isPresent();
//        Map<String, Object> response = new HashMap<>();
//        response.put("available", !isTaken);
//        return ResponseEntity.ok(response);
//    }



}

