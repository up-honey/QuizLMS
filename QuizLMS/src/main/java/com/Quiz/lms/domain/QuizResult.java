package com.Quiz.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 사용
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "correct")
    private boolean correct;

    @Column(name = "answer_given")
    private String answerGiven;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
