package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando un usuario intenta crear más proyectos de los permitidos
 */
public class ProjectLimitExceededException extends RuntimeException {
    
    public ProjectLimitExceededException(Long userId, int currentCount, int maxAllowed) {
        super("El usuario " + userId + " ya tiene " + currentCount + 
              " proyectos y no puede crear más. Límite máximo: " + maxAllowed + " proyectos");
    }
    
    public ProjectLimitExceededException(String message) {
        super(message);
    }
} 