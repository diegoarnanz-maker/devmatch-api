package com.devmatch.api.shared.domain.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String field, String message) {
        super("Error de validación en el campo '" + field + "': " + message);
    }
    
    public ValidationException(String entity, String field, String message) {
        super("Error de validación en " + entity + ", campo '" + field + "': " + message);
    }
}
