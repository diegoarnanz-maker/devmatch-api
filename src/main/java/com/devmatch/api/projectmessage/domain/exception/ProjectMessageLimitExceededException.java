package com.devmatch.api.projectmessage.domain.exception;

/**
 * Excepción lanzada cuando se excede el límite de mensajes permitidos.
 */
public class ProjectMessageLimitExceededException extends RuntimeException {
    
    public ProjectMessageLimitExceededException(String message) {
        super(message);
    }
    
    public ProjectMessageLimitExceededException(Long projectId, int limit) {
        super(String.format("Se ha excedido el límite de mensajes (%d) para el proyecto %d", limit, projectId));
    }
    
    public ProjectMessageLimitExceededException(Long userId, Long projectId, int limit) {
        super(String.format("Usuario %d ha excedido el límite de mensajes (%d) en el proyecto %d", userId, limit, projectId));
    }
}
