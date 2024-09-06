package com.Quiz.lms.repository;

import com.Quiz.lms.domain.SelectedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectedQuizRepository extends JpaRepository<SelectedQuiz, Long> {
    List<Long> findQuizIdsByCategoryName(String categoryName);
    void deleteByCategoryName(String categoryName);
}