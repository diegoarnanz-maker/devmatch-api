package com.devmatch.api.tag.domain.exception;

/**
 * Excepción lanzada cuando un tag está en uso y no se puede eliminar.
 */
public class TagInUseException extends RuntimeException {
    
    public TagInUseException(String message) {
        super(message);
    }
    
    public TagInUseException(Long tagId) {
        super("No se puede eliminar el tag con ID " + tagId + " porque está siendo utilizado por usuarios");
    }
} 