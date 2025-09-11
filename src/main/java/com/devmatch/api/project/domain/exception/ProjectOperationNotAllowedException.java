package com.devmatch.api.project.domain.exception;

/**
 * Excepción cuando operación no permitida en proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectOperationNotAllowedException extends RuntimeException {
    
    public ProjectOperationNotAllowedException(String message) {
        super(message);
    }
    
    public ProjectOperationNotAllowedException(Long projectId, String operation) {
        super("No se puede " + operation + " el proyecto con ID " + projectId);
    }
    
    public ProjectOperationNotAllowedException(Long projectId, Long userId, String operation) {
        super("El usuario " + userId + " no puede " + operation + " el proyecto " + projectId);
    }
} 