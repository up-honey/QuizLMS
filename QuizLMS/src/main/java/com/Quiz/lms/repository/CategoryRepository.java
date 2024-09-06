package com.Quiz.lms.repository;

import com.Quiz.lms.domain.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);


    void deleteById(long id);
    
    // 페이징을 위한 메서드 추가
    @Query(value = "SELECT * FROM ( " +
            "SELECT a.*, ROWNUM rnum FROM ( " +
            "SELECT * FROM Category ORDER BY id " +
            ") a WHERE ROWNUM <= :endRow " +
            ") WHERE rnum > :startRow", 
    nativeQuery = true)
Page<Category> findAllWithPagination(@Param("startRow") int startRow, 
                                  @Param("endRow") int endRow, 
                                  Pageable pageable);

}
