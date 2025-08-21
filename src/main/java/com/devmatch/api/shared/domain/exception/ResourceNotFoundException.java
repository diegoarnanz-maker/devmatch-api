package com.devmatch.api.shared.domain.exception;

/**
 * Excepción general lanzada cuando no se encuentra un recurso
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String resourceType, Long id) {
        super("No se encontró el " + resourceType + " con ID: " + id);
    }
    
    public ResourceNotFoundException(String resourceType, String identifier) {
        super("No se encontró el " + resourceType + " con identificador: " + identifier);
    }
    
    public ResourceNotFoundException(String resourceType, String field, String value) {
        super("No se encontró el " + resourceType + " con " + field + ": " + value);
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
