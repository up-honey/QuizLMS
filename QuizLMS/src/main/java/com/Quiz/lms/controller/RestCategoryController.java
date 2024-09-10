//package com.Quiz.lms.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.Quiz.lms.domain.Category;
//import com.Quiz.lms.dto.CategoryForm;
//import com.Quiz.lms.service.CategoryService;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/category")
//@RestController
//public class RestCategoryController {
//    
//    private final CategoryService categoryService;
//    
//    // 카테고리 등록 처리
//    @PostMapping("/create")
//    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CategoryForm categoryForm) {
//        categoryService.create(categoryForm.getName());
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Category created successfully");
//        System.out.println(categoryForm.getName()); // 백엔드에서 값이 제대로 넘어오는지 확인
//
//        return ResponseEntity.ok(response);
//    }
//    
// // 카테고리 수정 처리
//    @PostMapping("/modify/{id}")
//    public ResponseEntity<Map<String, String>> modify(@PathVariable("id") Long id,
//                                                       @RequestBody CategoryForm categoryForm) {
//        Category category = categoryService.getCategory(id);
//        categoryService.modify(category, categoryForm.getName());
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Category updated successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    // 카테고리 삭제
//    @GetMapping("/delete/{id}")
//    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id) {
//        categoryService.delete(id);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Category deleted successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    // 카테고리 목록 조회
//    @GetMapping("/list")
//    public ResponseEntity<Map<String, Object>> getCategories(@RequestParam(value="page", defaultValue = "0") int page,
//                                                              @RequestParam(value="size", defaultValue = "10") int size) {
//        Page<Category> categories = categoryService.getCategories(PageRequest.of(page, size));
//        Map<String, Object> response = new HashMap<>();
//        response.put("categories", categories.getContent());
//        response.put("totalPages", categories.getTotalPages());
//        response.put("currentPage", categories.getNumber());
//        return ResponseEntity.ok(response);
//    }
//}