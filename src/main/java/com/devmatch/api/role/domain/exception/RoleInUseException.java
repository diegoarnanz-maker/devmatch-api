package com.devmatch.api.role.domain.exception;

/**
 * Excepción lanzada cuando se intenta eliminar un rol que está en uso
 */
public class RoleInUseException extends RuntimeException {
    
    public RoleInUseException(String name) {
        super("No se puede eliminar el rol '" + name + "' porque está siendo utilizado por usuarios");
    }
    
    public RoleInUseException(Long id) {
        super("No se puede eliminar el rol con ID " + id + " porque está siendo utilizado por usuarios");
    }
} 