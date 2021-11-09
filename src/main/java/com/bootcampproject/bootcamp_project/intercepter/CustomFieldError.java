package com.bootcampproject.bootcamp_project.intercepter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomFieldError {
    private String field;
    private String error;
}
