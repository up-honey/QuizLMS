package com.Quiz.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "nickname", nullable = false, unique = true)
    @NotBlank(message = "Nickname is mandatory")
    private String nickname;
}
