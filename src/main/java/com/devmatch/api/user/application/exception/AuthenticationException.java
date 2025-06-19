package com.devmatch.api.user.application.exception;

/**
 * Excepción lanzada cuando hay un error en el proceso de autenticación.
 * Esta excepción es específica de la capa de aplicación y representa
 * errores en el flujo de autenticación.
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 