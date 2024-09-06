package com.Quiz.lms.repository;


import com.Quiz.lms.domain.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 카테고리 이름을 기준으로 퀴즈를 10개씩 페이징 처리해서 들고오는 메소드
    @Query("SELECT q FROM Quiz q WHERE q.category.name = :categoryName")
    Page<Quiz> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT q FROM Quiz q WHERE q.category.name = :categoryName AND q.id NOT IN :excludeIds")
    Page<Quiz> findByCategoryNameAndIdNotIn(
            @Param("categoryName") String categoryName,
            @Param("excludeIds") Set<Long> excludeIds,
            Pageable pageable
    );
}
