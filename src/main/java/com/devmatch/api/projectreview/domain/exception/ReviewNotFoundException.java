package com.devmatch.api.projectreview.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una review
 */
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super("No se encontró la review con ID: " + id);
    }
    public ReviewNotFoundException(String message) {
        super(message);
    }
} 