package com.Quiz.lms.controller;

import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class QuizResultController {

    private final QuizResultService quizResultService;

}