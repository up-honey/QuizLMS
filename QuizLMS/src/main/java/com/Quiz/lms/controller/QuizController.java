package com.Quiz.lms.controller;

import java.security.Principal;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.domain.SelectedQuiz;
import com.Quiz.lms.dto.CategoryForm;
import com.Quiz.lms.dto.QuizForm;
import com.Quiz.lms.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

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
        quizService.create(quizForm.getCategoryName(), quizForm.getTitle(), quizForm.getAnswer(),quizForm.getOptions());
        return "redirect:/quiz/list"; // 등록 후 목록 페이지로 리다이렉트
    }


    @GetMapping("/category/{categoryName}")
    public String getQuizzesByCategory(Model model, @PathVariable(value="categoryName") String categoryName) {
        Page<SelectedQuiz> quizzes = quizService.selectUniqueQuizzes(categoryName, 10);
        model.addAttribute("quizzes", quizzes.getContent());
        model.addAttribute("categoryName", categoryName);
        return "quiz-page";
    }


    @PostMapping("/submit")
    public String submitQuiz(@RequestParam("categoryName") String categoryName,
                             Principal pcp,
                             @RequestParam Map<String, String> answers,
                             Model model) {

        // 디버깅: 수신된 답변 로그 출력
        System.out.println("Received answers: " + answers);
        List<String> userAnswers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();
        List<Long> quizIds = new ArrayList<>();
        Long userId = memberRepository.findByUsername(pcp.getName()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")).getId();

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

        model.addAttribute("userId", userId);
        model.addAttribute("results", results);
        model.addAttribute("count", results.size());
        model.addAttribute("correctCount", correctCount);
        return "quiz-result"; // 결과 페이지로 이동
    }
    
    @GetMapping("/list") 
	 public String getQuizList(Model model, @RequestParam(value="page", defaultValue = "0") int page,
	 @RequestParam(value="size",defaultValue = "10") int size) { 
	 Page<Quiz> quizz = quizService.getQuiz(PageRequest.of(page, size));
	 //model.addAttribute("categories", categories.getContent());
	 model.addAttribute("paging", quizz); // Add paging information return
	 return "quiz_list";
	  }
    
    @GetMapping("/detail/{id}")
    public String getOneQuiz(@PathVariable("id") Long quizId, Model model) {
        Quiz quiz = quizService.getQuizById(quizId); // ID로 퀴즈 가져오기
        model.addAttribute("quiz", quiz); // 모델에 퀴즈 추가
        return "quiz_detail"; // 퀴즈 상세 페이지로 이동
    }

    // 퀴즈 수정 폼
    
    @GetMapping("/solution/{id}")
    public String getTrueQuiz(@PathVariable("id") Long quizId, Model model) {
        Quiz quiz = quizService.getQuizById(quizId); // ID로 퀴즈 가져오기
        Double radio = quizResultService.getQuizCorrectRadio(quizId);//정답률 계산하는 메소드
        model.addAttribute("radio", radio); // 모델에 정답률 추가
        model.addAttribute("quiz", quiz); // 모델에 퀴즈 추가
        return "quiz_solution"; // 퀴즈 상세 페이지로 이동
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

    // 퀴즈 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        quizService.delete(id);
        return "redirect:/quiz/list"; // 삭제 후 목록 페이지로 리다이렉트
    }
}