package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private boolean isActive;
    private boolean isLocked;

}
