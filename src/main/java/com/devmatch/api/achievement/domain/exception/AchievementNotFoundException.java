package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un logro específico.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementNotFoundException extends RuntimeException {
    
    public AchievementNotFoundException(Long id) {
        super("No se encontró el achievement con ID: " + id);
    }
    
    public AchievementNotFoundException(String code) {
        super("No se encontró el achievement con código: " + code);
    }
}
