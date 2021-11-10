package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetPasswordDTO {

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;
}
