package com.Quiz.lms.repository;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.domain.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUserIdAndQuizCategoryName(Long userId, String categoryName);
}
