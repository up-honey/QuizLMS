package com.Quiz.lms.controller;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.CategoryForm;
import com.Quiz.lms.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 등록 폼
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "category_regist"; // 카테고리 등록 페이지
    }

    // 카테고리 등록 처리
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoryForm") CategoryForm categoryForm, 
                         BindingResult bindingResult, 
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "category_regist"; // 오류가 있을 경우 등록 페이지로 돌아감
        }
        categoryService.create(categoryForm.getName());
        return "redirect:/category/list"; // 등록 후 목록 페이지로 리다이렉트
    }

    // 카테고리 수정 폼
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategory(id);
        model.addAttribute("categoryForm", category);
        return "category_modify"; // 카테고리 수정 페이지
    }

    // 카테고리 수정 처리
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, 
                         @ModelAttribute("categoryForm") CategoryForm categoryForm) {
        Category category = categoryService.getCategory(id);
        categoryService.modify(category, categoryForm.getName());
        return "redirect:/category/list"; // 수정 후 목록 페이지로 리다이렉트
    }

    // 카테고리 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/category/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

	 
	 @GetMapping("/list") 
	 public String getCategories(Model model, @RequestParam(value="page", defaultValue = "0") int page,
	 @RequestParam(value="size",defaultValue = "10") int size) { 
	 Page<Category> categories = categoryService.getCategories(PageRequest.of(page, size));
	 //model.addAttribute("categories", categories.getContent());
	 model.addAttribute("paging", categories); // Add paging information return
	 return "category_list";
	  }
	
}


