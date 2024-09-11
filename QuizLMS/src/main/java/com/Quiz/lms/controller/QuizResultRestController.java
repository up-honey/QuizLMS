package com.Quiz.lms.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.QuizResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class QuizResultRestController {

    private final QuizResultService quizResultService;
    private final MemberRepository memberRepository;
    
    // 퀴즈 결과 저장
    @PostMapping("/save")
    public ResponseEntity<List<QuizResult>> saveQuizResults(
            @RequestParam(value="userId") Long userId,
            @RequestParam(value="categoryName") String categoryName,
            @RequestParam(value="userAnswers") List<String> userAnswers) {
        List<QuizResult> results = quizResultService.takeQuizAndSaveResults(userId, categoryName, userAnswers);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<QuizResult>> getQuizResults(
            Principal pcp,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size) {
        
        long userId = memberRepository.findByUsername(pcp.getName()).get().getId();
        
        // 사용자별 전체 퀴즈 결과 조회
        List<QuizResult> allQuizResults = quizResultService.getAllResultsByUserId(userId);
        
        // 페이지 처리
        List<QuizResult> pagedResults = allQuizResults.stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        Page<QuizResult> quizResults = new PageImpl<>(pagedResults, PageRequest.of(page, size), allQuizResults.size());

        return ResponseEntity.ok(quizResults);
    }
}
