package com.Quiz.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 사용
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
