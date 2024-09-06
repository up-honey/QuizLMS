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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quizResult_seq")
    @SequenceGenerator(name = "quizResult_seq", sequenceName = "QUIZRESULT_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "answer_given")
    private String answerGiven;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
