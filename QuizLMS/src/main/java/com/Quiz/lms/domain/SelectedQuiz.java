package com.Quiz.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SelectedQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_seq")
    @SequenceGenerator(name = "quiz_seq", sequenceName = "QUIZ_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "quiz_id")
    private Long quizId;

}
