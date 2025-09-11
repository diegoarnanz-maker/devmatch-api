package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando ya existe una aplicación al proyecto.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectApplicationAlreadyExistsException extends RuntimeException {
    
    public ProjectApplicationAlreadyExistsException(Long projectId, Long userId) {
        super("El usuario " + userId + " ya tiene una aplicación al proyecto " + projectId);
    }
    
    public ProjectApplicationAlreadyExistsException(Long projectId, Long userId, String status) {
        super("El usuario " + userId + " ya tiene una aplicación al proyecto " + projectId + " con estado: " + status);
    }
}
