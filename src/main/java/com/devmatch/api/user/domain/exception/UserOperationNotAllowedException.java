package com.devmatch.api.user.domain.exception;

public class UserOperationNotAllowedException extends RuntimeException {
    public UserOperationNotAllowedException(String message) {
        super(message);
    }
} 