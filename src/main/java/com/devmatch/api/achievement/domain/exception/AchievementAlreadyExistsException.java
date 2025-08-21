package com.devmatch.api.achievement.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear un achievement que ya existe
 */
public class AchievementAlreadyExistsException extends RuntimeException {
    
    public AchievementAlreadyExistsException(String code) {
        super("Ya existe un achievement con el código: " + code);
    }
    
    public AchievementAlreadyExistsException(String code, String name) {
        super("Ya existe un achievement con el código '" + code + "' y nombre '" + name + "'");
    }
}
