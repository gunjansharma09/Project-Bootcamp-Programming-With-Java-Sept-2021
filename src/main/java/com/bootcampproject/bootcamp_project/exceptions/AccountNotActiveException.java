package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.security.authentication.AccountStatusException;

public class AccountNotActiveException extends AccountStatusException {

	public AccountNotActiveException(String message) {
		super(message);
	}
}
