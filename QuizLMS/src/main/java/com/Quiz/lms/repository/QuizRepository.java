package com.Quiz.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Quiz.lms.domain.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
	
	
	Quiz findById(long id);
	
    Page<Quiz> findByCategoryName(String categoryName, Pageable pageable);
    
    @Query("SELECT q FROM Quiz q WHERE q.category.name = :categoryName AND q.id NOT IN :ids")
    Page<Quiz> findByCategoryNameAndIdNotIn(@Param("categoryName") String categoryName, @Param("ids") Set<Long> ids, Pageable pageable);

    // 페이징을 위한 메서드 추가
    @Query(value = "SELECT * FROM Quiz ORDER BY id LIMIT :limit OFFSET :offset", 
           nativeQuery = true)
    List<Quiz> findAllWithPagination(@Param("offset") int offset,
                                      @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM Quiz", nativeQuery = true)
    long countQuiz();

    @Query(value = "SELECT * FROM Quiz ORDER BY id LIMIT :limit OFFSET :offset", 
           nativeQuery = true)
    Page<Quiz> findAllWithPagination(@Param("offset") int offset, 
                                      @Param("limit") int limit, 
                                      Pageable pageable);
}
