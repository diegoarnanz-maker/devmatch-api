package com.devmatch.api.user.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra el perfil de un usuario
 */
public class UserProfileNotFoundException extends RuntimeException {
    
    public UserProfileNotFoundException(Long userId) {
        super("No se encontró el perfil del usuario con ID: " + userId);
    }
    
    public UserProfileNotFoundException(String username) {
        super("No se encontró el perfil del usuario con username: " + username);
    }
    
    public UserProfileNotFoundException(Long userId, String profileType) {
        super("No se encontró el perfil de tipo '" + profileType + "' para el usuario " + userId);
    }
}
