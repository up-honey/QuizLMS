package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.CategoryForm;
import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.service.CategoryService;
import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;
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