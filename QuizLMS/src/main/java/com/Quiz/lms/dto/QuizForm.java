package com.Quiz.lms.dto;

import com.Quiz.lms.domain.Category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizForm {
    private String title;
    private String categoryName;
    private String answer;
    
    @NotNull(message = "카테고리는 필수 입력 항목입니다.")
    private Long categoryId;
}