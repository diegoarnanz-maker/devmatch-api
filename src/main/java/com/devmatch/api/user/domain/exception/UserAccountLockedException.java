package com.devmatch.api.user.domain.exception;

/**
 * Excepción lanzada cuando la cuenta de usuario está bloqueada
 */
public class UserAccountLockedException extends RuntimeException {
    
    public UserAccountLockedException(Long userId) {
        super("La cuenta del usuario " + userId + " está bloqueada");
    }
    
    public UserAccountLockedException(String username) {
        super("La cuenta del usuario '" + username + "' está bloqueada");
    }
    
    public UserAccountLockedException(Long userId, String reason) {
        super("La cuenta del usuario " + userId + " está bloqueada. Razón: " + reason);
    }
}
