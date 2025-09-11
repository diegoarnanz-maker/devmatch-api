package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción del dominio que se lanza cuando un usuario ya posee un logro específico.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class UserAlreadyHasAchievementException extends RuntimeException {
    
    public UserAlreadyHasAchievementException(Long userId, Long achievementId) {
        super(String.format("El usuario %d ya tiene el achievement %d", userId, achievementId));
    }
    
    public UserAlreadyHasAchievementException(Long userId, String achievementCode) {
        super(String.format("El usuario %d ya tiene el achievement '%s'", userId, achievementCode));
    }
}
