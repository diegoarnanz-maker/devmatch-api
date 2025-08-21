package com.devmatch.api.security.domain.exception;

/**
 * Excepción lanzada cuando hay errores relacionados con JWT tokens
 */
public class JwtTokenException extends RuntimeException {
    
    public JwtTokenException(String message) {
        super(message);
    }
    
    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JwtTokenException(String token, String reason) {
        super("Error en el token JWT: " + reason + " - Token: " + token);
    }
}
