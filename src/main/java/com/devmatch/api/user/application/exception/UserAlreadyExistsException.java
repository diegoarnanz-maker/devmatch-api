package com.devmatch.api.user.application.exception;

/**
 * Excepción lanzada cuando se intenta crear un usuario que ya existe.
 * Esta excepción es específica de la capa de aplicación y representa
 * errores en el flujo de registro de usuarios.
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 