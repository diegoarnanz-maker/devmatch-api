package com.devmatch.api.projectmessage.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un mensaje del proyecto.
 */
public class ProjectMessageNotFoundException extends RuntimeException {
    
    public ProjectMessageNotFoundException(Long messageId) {
        super(String.format("Mensaje del proyecto con ID %d no encontrado", messageId));
    }
    
    public ProjectMessageNotFoundException(String message) {
        super(message);
    }
}
