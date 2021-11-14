package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoPasswordFoundException extends RuntimeException {
    public NoPasswordFoundException(String message) {
        super(message);
    }
}
