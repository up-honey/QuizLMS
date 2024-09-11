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


    //이때 @ElementCollection은 options 테이블을 사용해 퀴즈의 옵션을 저장합니다. CascadeType.REMOVE 대신 기본적으로 orphanRemoval=true가 내장되어 있어 퀴즈를 삭제할 때 options도 함께 삭제됩니다.
    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "quiz_option")  // 'option' 대신 'quiz_option'으로 변경
    private List<String> options = new ArrayList<>();


}
