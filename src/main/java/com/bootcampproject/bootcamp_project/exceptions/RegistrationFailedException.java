package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message, HttpStatus notAcceptable) {
        super(message);
    }
}
