package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.dto.QuizForm;
import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizResultService quizResultService;
   
    // 퀴즈 등록 폼
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("quizForm", new QuizForm());
        return "quiz_regist"; // 카테고리 등록 페이지
    }

    // 퀴즈 등록 처리
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("quizForm") QuizForm quizForm, 
                         BindingResult bindingResult, 
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "quiz_regist"; // 오류가 있을 경우 등록 페이지로 돌아감
        }
        quizService.create(quizForm.getCategory().getName(), quizForm.getName(), quizForm.getAnswer());
        return "redirect:/quiz/list"; // 등록 후 목록 페이지로 리다이렉트
    }


    @GetMapping("/category/{categoryName}")
    public String getQuizzesByCategory(@PathVariable String categoryName, Model model) {
        Page<Quiz> quizzes = quizService.selectUniqueQuizzes(categoryName, 10);
        model.addAttribute("quizzes", quizzes.getContent());
        model.addAttribute("categoryName", categoryName);
        return "quiz-page";
    }

    @PostMapping("/submit")
    public String submitQuiz(@RequestParam String categoryName,
                             @RequestParam Long userId,
                             @RequestParam Map<String, String> answers,
                             Model model) {
        List<String> userAnswers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userAnswers.add(answers.get("answer" + i));
        }

        List<QuizResult> results = quizResultService.takeQuizAndSaveResults(userId, categoryName, userAnswers);

        model.addAttribute("results", results);
        return "quiz-result";
    }

}