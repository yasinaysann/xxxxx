package com.project.projectx.exceptionHandler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticatedFailedException extends RuntimeException {
    public AuthenticatedFailedException(String message) {
        super(message);
    }
}
