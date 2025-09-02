package com.devmatch.api.projectmessage.domain.exception;

/**
 * Excepción lanzada cuando una operación no está permitida en un mensaje del proyecto.
 */
public class ProjectMessageOperationNotAllowedException extends RuntimeException {
    
    public ProjectMessageOperationNotAllowedException(String message) {
        super(message);
    }
    
    public ProjectMessageOperationNotAllowedException(Long messageId, String operation) {
        super(String.format("Operación '%s' no permitida en el mensaje %d", operation, messageId));
    }
    
    public ProjectMessageOperationNotAllowedException(Long messageId, Long userId, String operation) {
        super(String.format("Usuario %d no puede realizar la operación '%s' en el mensaje %d", userId, operation, messageId));
    }
}
