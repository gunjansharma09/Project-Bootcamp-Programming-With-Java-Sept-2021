package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDeactivateException extends AccountStatusException {

    public UserDeactivateException(String message) {
        super(message);
    }
}
