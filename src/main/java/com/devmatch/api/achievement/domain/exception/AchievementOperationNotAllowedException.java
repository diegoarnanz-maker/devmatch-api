package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación no permitida en un achievement
 */
public class AchievementOperationNotAllowedException extends RuntimeException {
    
    public AchievementOperationNotAllowedException(String message) {
        super(message);
    }
    
    public AchievementOperationNotAllowedException(Long achievementId, String operation) {
        super("No se puede " + operation + " el achievement con ID " + achievementId);
    }
    
    public AchievementOperationNotAllowedException(String code, String operation) {
        super("No se puede " + operation + " el achievement con código " + code);
    }
}
