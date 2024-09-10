package com.Quiz.lms.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.SelectedQuiz;
import com.Quiz.lms.repository.CategoryRepository;
import com.Quiz.lms.repository.QuizRepository;
import com.Quiz.lms.repository.SelectedQuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class QuizService {

    private final QuizRepository quizRepository;
    private final SelectedQuizRepository selectedQuizRepository;
    private final CategoryRepository categoryRepository;

    //    퀴즈 등록
    @Transactional
    public Quiz create(String CategoryName, String title, String answer, List<String> options  ){
       // 카테고리 이름으로 카테고리 레포지토리에서 카테고리를 찾아옴
        Category category = categoryRepository.findByName(CategoryName);
        // 퀴즈를 등록하기 위해 새로운 퀴즈를 생성후 값 세팅
        Quiz quiz = new Quiz();
        quiz.setCategory(category);
        quiz.setTitle(title);
        quiz.setAnswer(answer);
        quiz.setOptions(options);
        // 생성한 퀴즈를 레포지토리를 사용하여 저장
        return quizRepository.save(quiz);
    }
    
    //퀴즈 정답을 들고오는 메소드
    public String getCorrectAnswer(long id) {
    	return quizRepository.findById(id).getAnswer();
    	
    }
    public Quiz getQuizById(long id) {
    	return quizRepository.findById(id);
    }


    // 카테고리 별로 퀴즈 10개 뽑기
    public Page<Quiz> selectTenQuiz(String CategoryName,int pageNumber,int pageSize){
      Page<Quiz> quizPage = quizRepository.findByCategoryName(CategoryName, PageRequest.of(pageNumber, pageSize));
      return quizPage;
    }

    @Transactional
    public Page<SelectedQuiz> selectUniqueQuizzes(String categoryName, int pageSize) {
        // 선택된 퀴즈 ID 목록을 가져옵니다.
        List<Long> selectedIds = selectedQuizRepository.findQuizIdsByCategoryName(categoryName);

        // 퀴즈를 가져오는 대신 SelectedQuiz로 변경
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

        // 새로 선택된 퀴즈를 저장할 리스트
        List<SelectedQuiz> selectedQuizList = new ArrayList<>();
        
        quizPage.getContent().forEach(quiz -> {
            SelectedQuiz selectedQuiz = new SelectedQuiz();
            selectedQuiz.setCategoryName(categoryName);
            selectedQuiz.setQuiz(quiz); // Quiz 객체를 설정
            selectedQuiz = selectedQuizRepository.save(selectedQuiz); // DB에 저장한 SelectedQuiz 객체를 다시 가져옴
            selectedQuizList.add(selectedQuiz); // 추가된 SelectedQuiz를 리스트에 저장
        });

        // SelectedQuiz 리스트를 Page로 변환
        return new PageImpl<>(selectedQuizList, quizPage.getPageable(), quizPage.getTotalElements());
    }

    
    
    public Page<Quiz> getQuiz(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;
        
        List<Quiz> categories = quizRepository.findAllWithPagination(startRow, endRow);
        long total = categoryRepository.countCategories();
        
        return new PageImpl<>(categories, pageable, total);
    }
    
    public Quiz getQuiz(Long id){
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

    }
    
//  퀴즈 수정
	/*
	 * @Transactional public Quiz modify(Long id, String title, String categoryName,
	 * String answer) { Quiz quiz = quizRepository.findById(id) .orElseThrow(() ->
	 * new ResourceNotFoundException("Quiz not found with id: " + id));
	 * 
	 * Category category = categoryRepository.findByName(categoryName)
	 * .orElseThrow(() -> new
	 * ResourceNotFoundException("Category not found with name: " + categoryName));
	 * 
	 * quiz.setTitle(title); quiz.setCategory(category); quiz.setAnswer(answer);
	 * 
	 * return quizRepository.save(quiz); }
	 */
//    public void modify( Long id, String title, String categoryName, String answer){
//    	Optional<Quiz> quiz = quizRepository.findById(id);
//    	
//    	Category category = categoryRepository.findByName(categoryName);
//    	
//        quiz.get().setTitle(title);
//    	
//        quiz.get().setCategory(category);
//        quiz.get().setAnswer(answer);
//        quizRepository.save(quiz.get());
//    }
    
//  카테고리 삭제
    public void delete(Long id){
       quizRepository.deleteById(id);
    }

}
