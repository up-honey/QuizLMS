package com.Quiz.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 로그인 폼으로 이동
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // 회원가입 폼으로 이동
    }
}
