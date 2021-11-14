package com.bootcampproject.bootcamp_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {

    @NotNull
    @NotEmpty(message = "First name can not be empty!")
    private String firstName;

    @NotNull
    @NotEmpty(message = "Last name can not be empty!")
    private String lastName;

    @NotNull
    @NotEmpty(message = "Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty(message = "Email should be valid!")
    private String email;
}
