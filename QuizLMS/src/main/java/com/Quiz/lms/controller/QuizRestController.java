package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.dto.QuizForm;
import com.Quiz.lms.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {

    private final QuizService quizService;

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
        quizService.modify(id, quizForm.getTitle(), quizForm.getCategoryName(), quizForm.getAnswer());
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
