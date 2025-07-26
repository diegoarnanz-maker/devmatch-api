package com.devmatch.api.projectreview.domain.exception;

/**
 * Excepción lanzada cuando una operación de reseña no está permitida
 */
public class ReviewOperationNotAllowedException extends RuntimeException {
    
    public ReviewOperationNotAllowedException(String message) {
        super(message);
    }
    
    public ReviewOperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Excepción específica para cuando se intenta crear una reseña en un proyecto que no está completado
     */
    public static ReviewOperationNotAllowedException projectNotCompleted(Long projectId) {
        return new ReviewOperationNotAllowedException(
            "No se puede crear una reseña para el proyecto con ID " + projectId + 
            ". Solo se permiten reseñas en proyectos con estado COMPLETED."
        );
    }
    
    /**
     * Excepción específica para cuando el usuario no tiene permisos para realizar la operación
     */
    public static ReviewOperationNotAllowedException insufficientPermissions(Long userId, Long reviewId) {
        return new ReviewOperationNotAllowedException(
            "El usuario con ID " + userId + " no tiene permisos para realizar esta operación en la reseña con ID " + reviewId
        );
    }
} 