package com.Quiz.lms.controller;

import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.service.MemberService;
import com.Quiz.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "register"; // 회원가입 폼으로 이동
    }

    @PostMapping("/register")
    public String register(MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        try {
            memberService.register(memberDTO); // 회원가입 처리
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "login"; // 로그인 폼으로 이동
    }

    @PostMapping("/login")
    public String login(MemberDTO memberDTO) {
        // 로그인 처리 로직 추가
        return "redirect:/";
    }

    // 중복 검사 메소드 추가
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
