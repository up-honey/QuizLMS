package com.Quiz.lms.dto;

import java.util.List;

import com.Quiz.lms.domain.Category;
import com.Quiz.lms.domain.SelectedQuiz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizForm {
    // Getters and Setters
    private String title;
    private String categoryName;
    private String answer;
    private List<String> options;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
