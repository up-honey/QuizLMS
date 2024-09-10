package com.Quiz.lms.controller;

import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/result")
@RequiredArgsConstructor
public class QuizResultController {

    private final QuizResultService quizResultService;
    
    
    // 퀴즈 결과 저장
    @PostMapping("/save")
    public ResponseEntity<List<QuizResult>> saveQuizResults(
            @RequestParam(value="userId") Long userId,
            @RequestParam(value="categoryName") String categoryName,
            @RequestParam(value="userAnswers") List<String> userAnswers) {
        List<QuizResult> results = quizResultService.takeQuizAndSaveResults(userId, categoryName, userAnswers);
        return ResponseEntity.ok(results);
    }

    // 사용자별 퀴즈 결과 조회
    @GetMapping("/user/{userId}/category/{categoryName}")
    public ResponseEntity<List<QuizResult>> getQuizResults(
            @PathVariable Long userId,
            @PathVariable String categoryName) {
        List<QuizResult> results = quizResultService.getQuizResults(userId, categoryName);
        return ResponseEntity.ok(results);
    }

}