package com.Quiz.lms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    // 비밀번호 확인 필드 추가
//    @NotBlank(message = "Password confirmation is mandatory")
//    private String passwordConfirmation;

//    @NotBlank(message = "Phone number is mandatory")
//    private String phoneNumber;
//
//    @NotBlank(message = "Email is mandatory")
//    @Email(message = "Email should be valid")
//    private String email;
//
//    @NotBlank(message = "Nickname is mandatory")
//    private String nickname;

    // 추가적인 필드 (예: 로그인 시 필요한 경우)
//    private String loginToken;
}


