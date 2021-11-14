package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.*;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserDto {
    @NotNull(message = "Email can not be null!")
    @Email(message = "Email should be valid!")
    private String email;
    @NotNull(message = "First name can not be null!")
    @Size(min = 2, max = 16)
    private String firstName;
    private String middleName;
    @NotNull(message = "Last name can not be null!")
    @Size(min = 2, max = 16)
    private String lastName;
    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    private String password;
    @NotNull(message = "password is mandatory")
    @NotBlank(message = "confirm password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}", message ="Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number!")
    private String confirmPassword;
    private String resetPassword;
    private boolean isActive;

}
