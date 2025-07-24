package com.devmatch.api.projectreview.domain.exception;

/**
 * Excepción lanzada cuando un usuario intenta crear más reviews de las permitidas
 */
public class ReviewLimitExceededException extends RuntimeException {
    public ReviewLimitExceededException(Long userId, int currentCount, int maxAllowed) {
        super("El usuario " + userId + " ya tiene " + currentCount +
              " reviews y no puede crear más. Límite máximo: " + maxAllowed + " reviews");
    }
    public ReviewLimitExceededException(String message) {
        super(message);
    }
} 