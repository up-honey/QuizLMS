package com.Quiz.lms.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {

    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final MemberRepository memberRepository;

    // 퀴즈 등록
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizForm quizForm) {
        quizService.create(quizForm.getCategoryName(), quizForm.getTitle(), quizForm.getAnswer(), quizForm.getOptions());
        return ResponseEntity.ok("Quiz created successfully");
    }

    // 카테고리 별 퀴즈 목록 조회
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<Quiz>> getQuizzesByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Quiz> quizzes = quizService.selectTenQuiz(categoryName, page, size);
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

    // 퀴즈 제출
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestParam("categoryName") String categoryName,
                                                           Principal principal,
                                                           @RequestParam Map<String, String> answers) {

        // 디버깅: 수신된 답변 로그 출력
        System.out.println("Received answers: " + answers);
        List<String> userAnswers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();
        List<Long> quizIds = new ArrayList<>();
        Long userId = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")).getId();

        // 퀴즈의 개수나 ID를 기반으로 로직을 수행
        int quizCount = answers.size() / 2; // 각 퀴즈마다 ID와 답변이 있으므로 /2
        for (int i = 0; i < quizCount; i++) {
            // 각 퀴즈 ID에 대한 사용자 답변을 가져옴
            String userAnswer = answers.get("answer_" + i);
            userAnswers.add(userAnswer);

            // 퀴즈 ID를 가져옴
            Long quizId = Long.valueOf(answers.get("quizId_" + i));
            quizIds.add(quizId);

            // 정답을 가져오는 로직
            String correctAnswer = quizService.getCorrectAnswer(quizId); // ID를 사용하여 정답을 가져옴
            correctAnswers.add(correctAnswer);
        }

        List<QuizResult> results = new ArrayList<>();

        // 정답 비교 및 결과 생성
        for (int i = 0; i < userAnswers.size(); i++) {
            boolean isCorrect = userAnswers.get(i).equals(correctAnswers.get(i));

            // QuizResult 객체 생성
            QuizResult quizResult = new QuizResult();
            quizResult.setUserId(userId);
            quizResult.setAnswerGiven(userAnswers.get(i));
            quizResult.setCorrect(isCorrect);
            quizResult.setTimestamp(LocalDateTime.now()); // 현재 시간 저장

            // Quiz 객체를 가져오는 로직
            Quiz quiz = quizService.getQuizById(quizIds.get(i)); // quizService에서 퀴즈 정보를 ID로 가져옴
            quizResult.setQuiz(quiz);

            results.add(quizResult);
        }

        // 결과를 저장
        quizResultService.saveResults(results);

        // 맞은 갯수 계산
        long correctCount = results.stream().filter(QuizResult::isCorrect).count();

        Map<String, Object> response = Map.of(
                "userId", userId,
                "results", results,
                "count", results.size(),
                "correctCount", correctCount
        );

        return ResponseEntity.ok(response);
    }

    // 퀴즈 정답률 조회
    @GetMapping("/solution/{id}")
    public ResponseEntity<Double> getQuizSolution(@PathVariable Long id) {
        Double radio = quizResultService.getQuizCorrectRadio(id); // 정답률 계산
        return ResponseEntity.ok(radio);
    }

    // 퀴즈 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<Quiz> getQuizDetail(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizById(id); // ID로 퀴즈 가져오기
        return ResponseEntity.ok(quiz); // 상세 정보를 반환
    }

    @GetMapping("/category/{categoryId}/random")
    public ResponseEntity<List<Quiz>> getRandomQuizzes(
            @PathVariable("categoryId") Long categoryId, 
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Quiz> quizzes = quizService.getRandomQuizzesByCategory(categoryId, limit);
        return ResponseEntity.ok(quizzes);
    }
}
