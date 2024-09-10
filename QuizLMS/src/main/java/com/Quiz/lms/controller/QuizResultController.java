package com.Quiz.lms.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.QuizResultService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/result")
@RequiredArgsConstructor
public class QuizResultController {

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
    public String getQuizResults(
            Principal pcp,
            Model model,
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

        model.addAttribute("paging", quizResults);
        
        // 전체 퀴즈 수와 맞춘 퀴즈 수 계산
        long totalQuizCount = allQuizResults.size(); // 전체 퀴즈 수
        long correctQuizCount = allQuizResults.stream()
                .filter(QuizResult::isCorrect) // 맞춘 퀴즈 필터링
                .count(); // 맞춘 퀴즈 수
        
        // 모델에 추가
        model.addAttribute("totalQuizCount", totalQuizCount);
        model.addAttribute("correctQuizCount", correctQuizCount);

        return "my_result";
    }

}