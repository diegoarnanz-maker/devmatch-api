package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un miembro del proyecto
 */
public class ProjectMemberNotFoundException extends RuntimeException {
    
    public ProjectMemberNotFoundException(Long id) {
        super("No se encontró el miembro del proyecto con ID: " + id);
    }
    
    public ProjectMemberNotFoundException(Long projectId, Long userId) {
        super("No se encontró al usuario " + userId + " como miembro del proyecto " + projectId);
    }
    
    public ProjectMemberNotFoundException(Long projectId, Long userId, String role) {
        super("No se encontró al usuario " + userId + " con rol '" + role + "' en el proyecto " + projectId);
    }
}
