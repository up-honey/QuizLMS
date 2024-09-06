package com.Quiz.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Quiz {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_seq")
    @SequenceGenerator(name = "quiz_seq", sequenceName = "QUIZ_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private  String title;

    @ManyToOne
    @JoinColumn(name = "category")
    private  Category category;

    @Column(name = "answer")
    private String answer;

}
