package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.dto.CategoryForm;
import com.Quiz.lms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    // 카테고리 목록 조회 (페이징 지원)
    @GetMapping("/list")
    public ResponseEntity<Page<Category>> getCategories(Pageable pageable) {
        Page<Category> categories = categoryService.getCategories(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 특정 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // 카테고리 생성
    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody CategoryForm categoryForm) {
        categoryService.create(categoryForm.getName());
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    // 카테고리 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyCategory(@PathVariable("id") Long id, @RequestBody CategoryForm categoryForm) {
        Category category = categoryService.getCategory(id);
        categoryService.modify(category, categoryForm.getName());
        return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
    }

    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.NO_CONTENT);
    }
}
