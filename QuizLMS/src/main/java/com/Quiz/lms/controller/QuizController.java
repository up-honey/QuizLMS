package com.Quiz.lms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.dto.CategoryForm;
import com.Quiz.lms.dto.QuizForm;
import com.Quiz.lms.service.QuizResultService;
import com.Quiz.lms.service.QuizService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
        quizService.create(quizForm.getCategoryName(), quizForm.getTitle(), quizForm.getAnswer());
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
    
    @GetMapping("/list") 
	 public String getQuizList(Model model, @RequestParam(value="page", defaultValue = "0") int page,
	 @RequestParam(value="size",defaultValue = "10") int size) { 
	 Page<Quiz> quizz = quizService.getQuiz(PageRequest.of(page, size));
	 //model.addAttribute("categories", categories.getContent());
	 model.addAttribute("paging", quizz); // Add paging information return
	 return "quiz_list";
	  }

 // 퀴즈 수정 폼
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model) {
        Quiz quizz = quizService.getQuiz(id);
        QuizForm quizForm = new QuizForm();
        quizForm.setTitle(quizz.getTitle());
        quizForm.setAnswer(quizz.getAnswer());
        quizForm.setCategoryName(quizz.getCategory().getName());
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("id", id);
        return "quiz_modify"; // 카테고리 수정 페이지
    }

    // 퀴즈 수정 처리
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, 
                         @ModelAttribute("quizForm") QuizForm quizForm) {

        quizService.modify(id, quizForm.getTitle(), quizForm.getCategoryName(), quizForm.getAnswer());
        return "redirect:/quiz/list"; // 수정 후 목록 페이지로 리다이렉트
    }
    
    // 카테고리 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        quizService.delete(id);
        return "redirect:/quiz/list"; // 삭제 후 목록 페이지로 리다이렉트
    }
}