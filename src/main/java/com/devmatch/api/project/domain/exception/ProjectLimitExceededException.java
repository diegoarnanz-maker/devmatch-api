package com.devmatch.api.project.domain.exception;

/**
 * Excepción cuando se excede el límite de proyectos por usuario.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectLimitExceededException extends RuntimeException {
    
    public ProjectLimitExceededException(Long userId, int currentCount, int maxAllowed) {
        super("El usuario " + userId + " ya tiene " + currentCount + 
              " proyectos activos en desarrollo y no puede crear más. Límite máximo: " + maxAllowed + " proyectos activos");
    }
    
    public ProjectLimitExceededException(String message) {
        super(message);
    }
} 