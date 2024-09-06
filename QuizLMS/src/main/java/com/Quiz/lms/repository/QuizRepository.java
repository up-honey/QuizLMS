package com.Quiz.lms.repository;


import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

	Page<Quiz> findByCategoryName(String categoryName, Pageable pageable);
	
	Page<Quiz> findByCategoryNameAndIdNotIn(String categoryName, Set<Long> ids, Pageable pageable);
	
	 // 페이징을 위한 메서드 추가
	 @Query(value = "SELECT * FROM (SELECT a.*, ROWNUM rnum FROM (SELECT * FROM Quiz ORDER BY id) a WHERE ROWNUM <= :endRow) WHERE rnum > :startRow", 
	            nativeQuery = true)
	     List<Quiz> findAllWithPagination(@Param("startRow") int startRow,
	    		 								@Param("endRow") int endRow);
	    

     
     @Query(value = "SELECT COUNT(*) FROM Quiz", nativeQuery = true)
     long countQuiz();

     @Query(value = "SELECT * FROM ( " +
             "SELECT a.*, ROWNUM rnum FROM ( " +
             "SELECT * FROM Quiz ORDER BY id " +
             ") a WHERE ROWNUM <= :endRow " +
             ") WHERE rnum > :startRow", 
     nativeQuery = true)
 Page<Quiz> findAllWithPagination(@Param("startRow") int startRow, 
                                   @Param("endRow") int endRow, 
                                   Pageable pageable);

}
