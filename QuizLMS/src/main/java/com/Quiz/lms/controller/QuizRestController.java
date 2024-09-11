package com.Quiz.lms.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.dto.QuizForm;
import com.Quiz.lms.repository.MemberRepository;
import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {

    private final QuizService quizService;
    //private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final QuizResultService quizResultService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody Map<String, Object> payload,
                                        Principal principal) {
        System.out.println("Received payload: " + payload);
        String categoryName = (String) payload.get("categoryName");

        List<Map<String, Object>> answers = new ArrayList<>();

        // 단일 퀴즈 응답 처리
        Long quizId = Long.valueOf(payload.get("quizId").toString());
        Object answerObj = payload.get("answer");
        String userAnswer = (answerObj instanceof Integer) ? String.valueOf(answerObj) : (String) answerObj;

        Map<String, Object> singleAnswer = new HashMap<>();
        singleAnswer.put("quizId", quizId);
        singleAnswer.put("answer", userAnswer);
        answers.add(singleAnswer);

        System.out.println("Processed answers: " + answers);

        Long userId = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."))
                .getId();

        List<QuizResult> results = new ArrayList<>();

        for (Map<String, Object> answer : answers) {
            Quiz quiz = quizService.getQuizById((Long) answer.get("quizId"));
            String correctAnswer = quizService.getCorrectAnswer(quiz.getId());

            boolean isCorrect = answer.get("answer").equals(correctAnswer);

            QuizResult quizResult = new QuizResult();
            quizResult.setUserId(userId);
            quizResult.setAnswerGiven((String) answer.get("answer"));
            quizResult.setCorrect(isCorrect);
            quizResult.setTimestamp(LocalDateTime.now());
            quizResult.setQuiz(quiz);

            results.add(quizResult);
        }

        quizResultService.saveResults(results);

        long correctCount = results.stream().filter(QuizResult::isCorrect).count();

        Map<String, Object> response = Map.of(
                "userId", userId,
                "results", results,
                "count", results.size(),
                "correctCount", correctCount
        );

        return ResponseEntity.ok(response);
    }

    // 퀴즈 등록
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizForm quizForm) {
        quizService.create(quizForm.getCategoryName(), quizForm.getTitle(), quizForm.getAnswer(), quizForm.getOptions());
        return ResponseEntity.ok("Quiz created successfully");
    }

    // 카테고리 별 퀴즈 목록 조회
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<Quiz>> getQuizzesByCategory(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {
        Page<Quiz> quizzes = quizService.selectTenQuiz(categoryName, page, size);
        System.out.println(categoryName);
        return ResponseEntity.ok(quizzes);
    }

    // 퀴즈 ID로 퀴즈 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuiz(id);
        return ResponseEntity.ok(quiz);
    }

    // 퀴즈 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyQuiz(
            @PathVariable Long id,
            @RequestBody QuizForm quizForm) {
        // 퀴즈 수정 호출
        quizService.modify(id, quizForm.getTitle(), quizForm.getCategoryName(), quizForm.getAnswer(), quizForm.getOptions());
        return ResponseEntity.ok("Quiz modified successfully");
    }



    // 퀴즈 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.ok("Quiz deleted successfully");
    }

    // 전체 퀴즈 목록 조회 (페이징 처리)
    @GetMapping("/list")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {
        Page<Quiz> quizzes = quizService.getQuiz(PageRequest.of(page, size));
        return ResponseEntity.ok(quizzes);
    }
}
