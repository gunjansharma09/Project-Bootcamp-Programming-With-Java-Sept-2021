package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.LOCKED, value = HttpStatus.LOCKED)
public class AccountLockedException extends AccountStatusException {

	public AccountLockedException(String message) {
		super(message);
	}
}
