package com.Quiz.lms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Quiz.lms.dto.CategoryForm;
import com.Quiz.lms.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class RestCategoryController {
    
    private final CategoryService categoryService;
    
    // 카테고리 등록 처리
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CategoryForm categoryForm) {
        categoryService.create(categoryForm.getName());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category created successfully");
        System.out.println(categoryForm.getName()); // 백엔드에서 값이 제대로 넘어오는지 확인

        return ResponseEntity.ok(response);
    }
}