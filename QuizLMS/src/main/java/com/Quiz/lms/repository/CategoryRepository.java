package com.Quiz.lms.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Quiz.lms.domain.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
	 Page<Category> findAll(Pageable pageable);
	
	  
	 
	Category findByName(String name);

    void deleteById(long id);

    // 페이징을 위한 메서드 추가
    @Query(value = "SELECT * FROM Category ORDER BY id LIMIT :limit OFFSET :offset", 
           nativeQuery = true)
    List<Category> findAllWithPagination(@Param("offset") int offset,
                                         @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM Category", nativeQuery = true)
    long countCategories();

    @Query(value = "SELECT * FROM Category ORDER BY id LIMIT :limit OFFSET :offset", 
           nativeQuery = true)
    Page<Category> findAllWithPagination(@Param("offset") int offset, 
                                         @Param("limit") int limit, 
                                         Pageable pageable);

}
