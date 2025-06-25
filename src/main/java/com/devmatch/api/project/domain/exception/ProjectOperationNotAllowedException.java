package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación no permitida en un proyecto
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