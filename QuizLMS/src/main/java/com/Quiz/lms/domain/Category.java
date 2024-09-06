package com.Quiz.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_SEQ", allocationSize = 1)
   @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private  String name;
}
