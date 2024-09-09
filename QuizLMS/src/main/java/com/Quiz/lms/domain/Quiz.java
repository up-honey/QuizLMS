package com.Quiz.lms.domain;

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
    
    @ElementCollection // 선택지를 저장하기 위한 어노테이션
    @Column(name = "options") // 선택지 컬럼
    private List<String> options; // 선택지 리스트

}
