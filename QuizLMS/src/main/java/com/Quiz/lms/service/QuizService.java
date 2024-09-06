package com.Quiz.lms.service;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.SelectedQuiz;
import com.Quiz.lms.repository.CategoryRepository;
import com.Quiz.lms.repository.QuizRepository;
import com.Quiz.lms.repository.SelectedQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final SelectedQuizRepository selectedQuizRepository;
    private final CategoryRepository categoryRepository;

    //    퀴즈 등록
    public void create(String CategoryName, String title, String answer ){
       // 카테고리 이름으로 카테고리 레포지토리에서 카테고리를 찾아옴
        Category category = categoryRepository.findByName(CategoryName);
        // 퀴즈를 등록하기 위해 새로운 퀴즈를 생성후 값 세팅
        Quiz quiz = new Quiz();
        quiz.setCategory(category);
        quiz.setTitle(title);
        quiz.setAnswer(answer);
        // 생성한 퀴즈를 레포지토리를 사용하여 저장
        quizRepository.save(quiz);
    }

    // 카테고리 별로 퀴즈 10개 뽑기
    public Page<Quiz> selectTenQuiz(String CategoryName,int pageNumber,int pageSize){
      Page<Quiz> quizPage = quizRepository.findByCategoryName(CategoryName, PageRequest.of(pageNumber, pageSize));
      return quizPage;
    }

    // 카테고리 별로 퀴즈 10개를 뽑지만 중복이 안되게 하는 메소드(클루드 제공)
    @Transactional
    public Page<Quiz> selectUniqueQuizzes(String categoryName, int pageSize) {
        List<Long> selectedIds = selectedQuizRepository.findQuizIdsByCategoryName(categoryName);

        Page<Quiz> quizPage = quizRepository.findByCategoryNameAndIdNotIn(
                categoryName,
                new HashSet<>(selectedIds),
                PageRequest.of(0, pageSize)
        );

        if (quizPage.isEmpty()) {
            // 모든 퀴즈를 사용했다면 선택 기록을 초기화
            selectedQuizRepository.deleteByCategoryName(categoryName);
            quizPage = quizRepository.findByCategoryName(categoryName, PageRequest.of(0, pageSize));
        }

        // 새로 선택된 퀴즈 ID 저장
        quizPage.getContent().forEach(quiz -> {
            SelectedQuiz selectedQuiz = new SelectedQuiz();
            selectedQuiz.setCategoryName(categoryName);
            selectedQuiz.setQuizId(quiz.getId());
            selectedQuizRepository.save(selectedQuiz);
        });

        return quizPage;
    }


}
