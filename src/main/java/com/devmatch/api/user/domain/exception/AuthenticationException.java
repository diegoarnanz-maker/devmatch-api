package com.devmatch.api.user.domain.exception;

/**
 * Excepción lanzada cuando hay un error en el proceso de autenticación.
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 