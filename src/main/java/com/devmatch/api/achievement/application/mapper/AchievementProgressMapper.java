package com.devmatch.api.achievement.application.mapper;

import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.application.dto.AchievementProgressDto;

/**
 * Mapper para convertir entre entidades Achievement y DTOs de progreso.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementProgressMapper {
    
    /**
     * Convierte una entidad Achievement a AchievementProgressDto con progreso específico
     * 
     * @param achievement Entidad de dominio
     * @param currentProgress Progreso actual del usuario
     * @param requiredProgress Progreso requerido para desbloquear
     * @return DTO de progreso
     */
    public static AchievementProgressDto toProgressDto(
            Achievement achievement, 
            Integer currentProgress, 
            Integer requiredProgress) {
        
        if (achievement == null) {
            return null;
        }
        
        return new AchievementProgressDto(
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue(),
            currentProgress != null ? currentProgress : 0,
            requiredProgress != null ? requiredProgress : 1,
            achievement.getType().getValue()
        );
    }
    
    /**
     * Convierte una entidad Achievement a AchievementProgressDto con progreso por defecto
     * 
     * @param achievement Entidad de dominio
     * @return DTO de progreso con progreso 0
     */
    public static AchievementProgressDto toProgressDto(Achievement achievement) {
        return toProgressDto(achievement, 0, 1);
    }
    
    /**
     * Convierte una entidad Achievement a AchievementProgressDto para achievement ya desbloqueado
     * 
     * @param achievement Entidad de dominio
     * @return DTO de progreso con progreso completo
     */
    public static AchievementProgressDto toCompletedProgressDto(Achievement achievement) {
        if (achievement == null) {
            return null;
        }
        
        // Para achievements ya desbloqueados, el progreso es 100%
        int requiredProgress = achievement.getPoints().getValue(); // Usamos los puntos como progreso requerido
        return new AchievementProgressDto(
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue(),
            requiredProgress, // Progreso actual = requerido (100%)
            requiredProgress,
            achievement.getType().getValue()
        );
    }
}
