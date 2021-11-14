package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AlreadyExistsGSTException extends RuntimeException {
    public AlreadyExistsGSTException(String message) {
        super(message);
    }
}
