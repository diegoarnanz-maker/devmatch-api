package com.devmatch.api.user.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un tag.
 */
public class TagNotFoundException extends RuntimeException {
    
    public TagNotFoundException(String message) {
        super(message);
    }
    
    public TagNotFoundException(Long tagId) {
        super("Tag no encontrado con ID: " + tagId);
    }
} 