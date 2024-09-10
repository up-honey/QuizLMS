package com.Quiz.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.Quiz.lms.domain.Category;
import com.Quiz.lms.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

//    카테고리 등록
    public void create(String name){
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

    }

    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

    }
//  카테고리 수정
    public void modify(Category category, String name){
        category.setName(name);
        categoryRepository.save(category);
    }
//  카테고리 삭제
    public void delete(Long id){
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoryList(){
        return categoryRepository.findAll();
    }

    // 카테고리 목록 조회 (페이징 처리)
 
    
    public Page<Category> getCategories(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;
        
        List<Category> categories = categoryRepository.findAllWithPagination(startRow, endRow);
        long total = categoryRepository.countCategories();
        
        return new PageImpl<>(categories, pageable, total);
    }
    
    // 카테고리 목록 조회 (페이징 처리, PageRequest 직접 사용)

    
    public Page<Category> getCategory(int page) {
    	Pageable pageable = PageRequest.of(page, 10);
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startRow = pageNumber * pageSize;
        int endRow = startRow + pageSize;

        return categoryRepository.findAllWithPagination(startRow, endRow, pageable);
    }

}
