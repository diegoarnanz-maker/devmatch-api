package com.devmatch.api.role.domain.exception;

/**
 * Excepción lanzada cuando ya existe un rol con el mismo nombre
 */
public class RoleAlreadyExistsException extends RuntimeException {
    
    public RoleAlreadyExistsException(String name) {
        super("Ya existe un rol con el nombre: " + name);
    }
} 