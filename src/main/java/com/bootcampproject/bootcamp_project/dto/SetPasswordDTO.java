package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetPasswordDTO {

    @NotNull(message = "Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @NotNull(message = "Confirm password must be same as password!")
    private String confirmPassword;
}
