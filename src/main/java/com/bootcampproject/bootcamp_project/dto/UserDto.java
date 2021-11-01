package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class UserDto {
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;

}
