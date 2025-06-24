package com.devmatch.api.role.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un rol
 */
public class RoleNotFoundException extends RuntimeException {
    
    public RoleNotFoundException(Long id) {
        super("No se encontró el rol con ID: " + id);
    }
    
    public RoleNotFoundException(String name) {
        super("No se encontró el rol con nombre: " + name);
    }
} 