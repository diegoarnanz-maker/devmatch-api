package com.devmatch.api.user.domain.exception;

/**
 * Excepción lanzada cuando se intenta eliminar un tipo de perfil que está siendo utilizado por usuarios.
 */
public class ProfileTypeInUseException extends RuntimeException {
    
    public ProfileTypeInUseException(String message) {
        super(message);
    }
    
    public ProfileTypeInUseException(String message, Throwable cause) {
        super(message, cause);
    }
} 