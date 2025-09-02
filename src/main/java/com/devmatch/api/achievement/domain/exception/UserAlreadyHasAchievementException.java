package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción lanzada cuando un usuario ya tiene un achievement específico.
 */
public class UserAlreadyHasAchievementException extends RuntimeException {
    
    public UserAlreadyHasAchievementException(Long userId, Long achievementId) {
        super(String.format("El usuario %d ya tiene el achievement %d", userId, achievementId));
    }
    
    public UserAlreadyHasAchievementException(Long userId, String achievementCode) {
        super(String.format("El usuario %d ya tiene el achievement '%s'", userId, achievementCode));
    }
}
