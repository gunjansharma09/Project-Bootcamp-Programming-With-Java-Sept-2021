package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Data
public class UserDto {
    @Column(unique = true)
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 2, max = 16)
    private String firstName;
    private String middleName;
    @NotNull
    @Size(min = 2, max = 16)
    private String lastName;
    @NotNull
    @NotBlank(message = "password is mandatory")
    private String password;
    private String confirmPassword;
    private String resetPassword;
    private boolean isActive;

}
