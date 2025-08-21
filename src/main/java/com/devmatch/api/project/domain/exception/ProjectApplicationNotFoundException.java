package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una aplicación a proyecto
 */
public class ProjectApplicationNotFoundException extends RuntimeException {
    
    public ProjectApplicationNotFoundException(Long id) {
        super("No se encontró la aplicación al proyecto con ID: " + id);
    }
    
    public ProjectApplicationNotFoundException(Long projectId, Long userId) {
        super("No se encontró la aplicación del usuario " + userId + " al proyecto " + projectId);
    }
}
