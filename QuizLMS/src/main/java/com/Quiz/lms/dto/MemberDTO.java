package com.Quiz.lms.dto;

import java.util.Set;

import com.Quiz.lms.domain.UserRole;

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
    
    
    private Set<UserRole> roles;
}


