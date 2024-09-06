package com.Quiz.lms.dto;

import com.Quiz.lms.domain.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizForm {
    private String title;
    private String categoryName;
    private String answer;
}