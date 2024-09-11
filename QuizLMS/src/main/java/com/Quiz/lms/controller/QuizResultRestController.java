package com.Quiz.lms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.QuizResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/result")
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
    public ResponseEntity<QuizResultsResponse> getQuizResults(
            Principal pcp,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size) {
        
        long userId = memberRepository.findByUsername(pcp.getName()).get().getId();
        
        // 사용자별 전체 퀴즈 결과 조회
        List<QuizResult> allQuizResults = quizResultService.getAllResultsByUserId(userId);
        
        // 페이지 처리
        int start = Math.min((int) (page * size), allQuizResults.size());
        int end = Math.min((int) ((page + 1) * size), allQuizResults.size());
        List<QuizResult> pagedResults = allQuizResults.subList(start, end);

        long totalQuizCount = allQuizResults.size(); // 전체 퀴즈 수
        long correctQuizCount = allQuizResults.stream()
                .filter(QuizResult::isCorrect) // 맞춘 퀴즈 필터링
                .count(); // 맞춘 퀴즈 수
        
        QuizResultsResponse response = new QuizResultsResponse(pagedResults, totalQuizCount, correctQuizCount);
        
        return ResponseEntity.ok(response);
    }

    // 결과 응답을 위한 DTO 클래스
    public static class QuizResultsResponse {
        private List<QuizResult> results;
        private long totalQuizCount;
        private long correctQuizCount;

        public QuizResultsResponse(List<QuizResult> results, long totalQuizCount, long correctQuizCount) {
            this.results = results;
            this.totalQuizCount = totalQuizCount;
            this.correctQuizCount = correctQuizCount;
        }

        public List<QuizResult> getResults() {
            return results;
        }

        public long getTotalQuizCount() {
            return totalQuizCount;
        }

        public long getCorrectQuizCount() {
            return correctQuizCount;
        }
    }
}
