package com.devmatch.api.security.domain.exception;

/**
 * Excepción lanzada cuando un usuario no tiene permisos suficientes para realizar una operación
 */
public class InsufficientPermissionsException extends RuntimeException {
    
    public InsufficientPermissionsException(String message) {
        super(message);
    }
    
    public InsufficientPermissionsException(Long userId, String operation) {
        super("El usuario " + userId + " no tiene permisos para realizar la operación: " + operation);
    }
    
    public InsufficientPermissionsException(Long userId, String operation, String resource) {
        super("El usuario " + userId + " no tiene permisos para " + operation + " en " + resource);
    }
    
    public InsufficientPermissionsException(String username, String operation) {
        super("El usuario '" + username + "' no tiene permisos para realizar la operación: " + operation);
    }
}
