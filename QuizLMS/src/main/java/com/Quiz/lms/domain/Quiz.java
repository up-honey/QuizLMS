package com.Quiz.lms.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Quiz {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 사용
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private  String title;

    @ManyToOne
    @JoinColumn(name = "category")
    private  Category category;

    @Column(name = "answer")
    private String answer;

    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "quiz_option")  // 'option' 대신 'quiz_option'으로 변경
    private List<String> options = new ArrayList<>();


}
