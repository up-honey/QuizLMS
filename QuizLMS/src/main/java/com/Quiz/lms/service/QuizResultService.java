package com.Quiz.lms.service;

import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.domain.SelectedQuiz;
import com.Quiz.lms.repository.QuizRepository;
import com.Quiz.lms.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizResultService {

    private final QuizResultRepository quizResultRepository;
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    
    
    @Transactional
    public void saveResults(List<QuizResult> results) {
        quizResultRepository.saveAll(results);
    }


    // 사용자별로 퀴즈 결과를 저장하는 메소드
    @Transactional
    public List<QuizResult> takeQuizAndSaveResults(Long userId, String categoryName, List<String> userAnswers) {
        // 사용자가 제출한 답안의 수에 기반하여 퀴즈를 선택합니다.
        int numberOfQuizzesToSelect = userAnswers.size();
        Page<SelectedQuiz> selectedQuizzes = quizService.selectUniqueQuizzes(categoryName, numberOfQuizzesToSelect);
        List<SelectedQuiz> selectedQuizList = selectedQuizzes.getContent();

        // 선택된 퀴즈의 수와 사용자가 제출한 답안의 수가 일치하는지 확인합니다.
        if (selectedQuizList.size() != numberOfQuizzesToSelect) {
            throw new IllegalArgumentException("Number of answers does not match number of quizzes");
        }

        List<QuizResult> results = new ArrayList<>();
        for (int i = 0; i < selectedQuizList.size(); i++) {
            SelectedQuiz selectedQuiz = selectedQuizList.get(i);
            Quiz quiz = quizRepository.findById(selectedQuiz.getQuiz().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Quiz not found for ID: " + selectedQuiz.getQuiz().getId()));
            String userAnswer = userAnswers.get(i);

            QuizResult result = new QuizResult();
            result.setQuiz(quiz);
            result.setUserId(userId);
            result.setCorrect(userAnswer.equals(quiz.getAnswer()));
            result.setAnswerGiven(userAnswer);
            result.setTimestamp(LocalDateTime.now());

            results.add(result);
        }

        return quizResultRepository.saveAll(results);
    }


    public List<QuizResult> getQuizResults(Long userId, String categoryName) {
        return quizResultRepository.findByUserIdAndQuizCategoryName(userId, categoryName);
    }

}
