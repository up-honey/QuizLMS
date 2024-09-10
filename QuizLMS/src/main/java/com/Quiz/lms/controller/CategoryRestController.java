package com.Quiz.lms.controller;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.dto.CategoryForm;
import com.Quiz.lms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    // 카테고리 목록 조회 (페이징 지원)
    @GetMapping("/list")
    public Page<Category> getCategories(Pageable pageable) {
        return categoryService.getCategories(pageable);
    }

    // 특정 카테고리 조회
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    // 카테고리 생성
    @PostMapping("/create")
    public String createCategory(@RequestBody CategoryForm categoryForm) {
        categoryService.create(categoryForm.getName());
        return "Category created successfully";
    }

    // 카테고리 수정
    @PutMapping("/modify/{id}")
    public String modifyCategory(@PathVariable Long id, @RequestBody CategoryForm categoryForm) {
        Category category = categoryService.getCategory(id);
        categoryService.modify(category, categoryForm.getName());
        return "Category updated successfully";
    }

    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "Category deleted successfully";
    }
}
