package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.AdminUserAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;

import java.util.List;

/**
 * Puerto de entrada para gestión administrativa de logros de usuarios.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public interface AdminUserAchievementUseCase {
    
    /**
     * Obtiene todos los achievements de un usuario específico
     * 
     * @param userId ID del usuario
     * @return Lista de achievements del usuario
     */
    List<UserAchievementResponseDto> getUserAchievements(Long userId);
    
    /**
     * Asigna un achievement a un usuario
     * 
     * @param userId ID del usuario
     * @param request DTO con la información de asignación
     * @return UserAchievement creado
     */
    UserAchievementResponseDto assignAchievement(Long userId, AdminUserAchievementRequestDto request);
    
    /**
     * Remueve un achievement de un usuario
     * 
     * @param userId ID del usuario
     * @param achievementId ID del achievement a remover
     */
    void removeAchievement(Long userId, Long achievementId);
    
    /**
     * Fuerza la verificación de achievements para un usuario
     * 
     * @param userId ID del usuario
     * @return Lista de achievements desbloqueados
     */
    List<UserAchievementResponseDto> forceAchievementCheck(Long userId);
    
    /**
     * Verifica si un usuario tiene un achievement específico
     * 
     * @param userId ID del usuario
     * @param achievementId ID del achievement
     * @return true si el usuario tiene el achievement, false en caso contrario
     */
    boolean hasUserAchievement(Long userId, Long achievementId);
    
    /**
     * Obtiene el total de puntos de achievements de un usuario
     * 
     * @param userId ID del usuario
     * @return Total de puntos acumulados
     */
    int getUserTotalPoints(Long userId);
}
