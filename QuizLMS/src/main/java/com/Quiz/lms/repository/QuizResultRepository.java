package com.Quiz.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Quiz.lms.domain.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUserIdAndQuizCategoryName(Long userId, String categoryName);
    Page<QuizResult> findByUserId(Long userid, Pageable page);
    List<QuizResult> findByUserId(Long userid);
}
