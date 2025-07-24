package com.devmatch.api.projectreview.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación no permitida en una review
 */
public class ReviewOperationNotAllowedException extends RuntimeException {
    public ReviewOperationNotAllowedException(String message) {
        super(message);
    }
    public ReviewOperationNotAllowedException(Long reviewId, String operation) {
        super("No se puede " + operation + " la review con ID " + reviewId);
    }
    public ReviewOperationNotAllowedException(Long reviewId, Long userId, String operation) {
        super("El usuario " + userId + " no puede " + operation + " la review " + reviewId);
    }
} 