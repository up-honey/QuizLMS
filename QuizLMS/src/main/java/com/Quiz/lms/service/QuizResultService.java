package com.Quiz.lms.service;

import com.Quiz.lms.domain.Quiz;
import com.Quiz.lms.domain.QuizResult;
import com.Quiz.lms.domain.SelectedQuiz;
import com.Quiz.lms.repository.QuizRepository;
import com.Quiz.lms.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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



    public List<QuizResult> getQuizResults(Long userId, String categoryName) {
        return quizResultRepository.findByUserIdAndQuizCategoryName(userId, categoryName);
    }
    
    public Page<QuizResult> getMyResult(Long userId, Pageable pageable){
    	return quizResultRepository.findByUserId(userId, pageable);
    }
    
    
    
    public double getQuizCorrectRadio(Long quizID) {
        // 전체 퀴즈 결과를 가져옵니다.
        List<QuizResult> quizResults = quizResultRepository.findByQuizId(quizID);
        
        // 정답 수와 전체 수를 계산합니다.
        int correctCount = 0;
        int totalCount = quizResults.size();

        for (QuizResult result : quizResults) {
            if (result.isCorrect()) { // isCorrect() 메소드는 정답 여부를 반환한다고 가정합니다.
                correctCount++;
            }
        }

        // 정답률 계산
       double correctRate = (totalCount > 0) ? (double) correctCount / totalCount * 100 : 0.0;
       return correctRate;
    }
    
    
    
    
    public List<QuizResult> getAllResultsByUserId(Long userId){
    	
         // 모든 결과를 가져옵니다
         List<QuizResult> results = quizResultRepository.findByUserId(userId);

         // 중복 제거: quizId를 기준으로 correct가 true인 결과를 우선 선택
         Map<Long, QuizResult> distinctResults = results.stream()
                 .collect(Collectors.toMap(
                         qr -> qr.getQuiz().getId(), // quizId 기준
                         qr -> qr, // QuizResult 객체
                         (existing, replacement) -> existing.isCorrect() ? existing : replacement // correct가 true인 경우 유지
                 ));

         // 모든 결과를 리스트로 반환
         return distinctResults.values().stream()
                 .collect(Collectors.toList());
    }
    
    
    

}
