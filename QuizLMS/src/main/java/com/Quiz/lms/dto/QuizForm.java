package com.Quiz.lms.dto;

import java.util.List;

import com.Quiz.lms.domain.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizForm {
    private String title;
    private String categoryName;
    private String answer;
    private List<String> options; // 옵션 리스트 추가
}