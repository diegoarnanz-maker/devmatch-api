package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un user achievement
 */
public class UserAchievementNotFoundException extends RuntimeException {
    
    public UserAchievementNotFoundException(Long id) {
        super("No se encontró el user achievement con ID: " + id);
    }
    
    public UserAchievementNotFoundException(Long userId, Long achievementId) {
        super("No se encontró el achievement " + achievementId + " para el usuario " + userId);
    }
    
    public UserAchievementNotFoundException(Long userId, String achievementCode) {
        super("No se encontró el achievement con código '" + achievementCode + "' para el usuario " + userId);
    }
}
