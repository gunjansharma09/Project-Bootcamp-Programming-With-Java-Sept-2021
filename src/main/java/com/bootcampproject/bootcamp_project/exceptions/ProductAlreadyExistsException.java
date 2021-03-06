package com.bootcampproject.bootcamp_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class ProductAlreadyExistsException extends RuntimeException {

  public ProductAlreadyExistsException(String message) {
    super(message);
  }
}
