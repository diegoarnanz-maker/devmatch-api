package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;

import java.util.List;

/**
 * Puerto de entrada para la gestión de user achievements.
 * Define las operaciones relacionadas con achievements de usuarios.
 */
public interface UserAchievementUseCase {
    
    /**
     * Obtiene todos los achievements de un usuario
     * 
     * @param userId ID del usuario
     * @return Lista de DTOs de user achievements
     */
    List<UserAchievementResponseDto> getUserAchievements(Long userId);
    
    /**
     * Obtiene un user achievement específico
     * 
     * @param userId ID del usuario
     * @param achievementCode Código del achievement
     * @return DTO del user achievement
     */
    UserAchievementResponseDto getUserAchievement(Long userId, String achievementCode);
    
    /**
     * Verifica si un usuario tiene un achievement específico
     * 
     * @param userId ID del usuario
     * @param achievementCode Código del achievement
     * @return true si el usuario tiene el achievement, false en caso contrario
     */
    boolean hasUserAchievement(Long userId, String achievementCode);
    
    /**
     * Obtiene el total de puntos de achievements de un usuario
     * 
     * @param userId ID del usuario
     * @return Total de puntos acumulados
     */
    int getUserTotalPoints(Long userId);
}
