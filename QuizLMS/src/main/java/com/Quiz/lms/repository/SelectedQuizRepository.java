package com.Quiz.lms.repository;

import com.Quiz.lms.domain.SelectedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelectedQuizRepository extends JpaRepository<SelectedQuiz, Long> {
	
    @Query("SELECT sq.quiz.id FROM SelectedQuiz sq WHERE sq.categoryName = :categoryName")
    List<Long> findQuizIdsByCategoryName(@Param("categoryName") String categoryName);
	
	@Query("SELECT sq FROM SelectedQuiz sq WHERE sq.categoryName = :categoryName")
	List<SelectedQuiz> findSelectedQuizzesByCategoryName(@Param("categoryName") String categoryName);

    void deleteByCategoryName(String categoryName);
}