package com.devmatch.api.project.domain.exception;

/**
 * Excepción cuando no se encuentra aplicación a proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectApplicationNotFoundException extends RuntimeException {
    
    public ProjectApplicationNotFoundException(Long id) {
        super("No se encontró la aplicación al proyecto con ID: " + id);
    }
    
    public ProjectApplicationNotFoundException(Long projectId, Long userId) {
        super("No se encontró la aplicación del usuario " + userId + " al proyecto " + projectId);
    }
}
