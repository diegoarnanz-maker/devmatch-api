package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.AdminUserAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;

import java.util.List;

/**
 * Puerto de entrada para la gestión administrativa de achievements de usuarios.
 * Define las operaciones que solo pueden realizar administradores.
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
     * @param request DTO con la información de asignación
     * @return UserAchievement creado
     */
    UserAchievementResponseDto assignAchievement(AdminUserAchievementRequestDto request);
    
    /**
     * Remueve un achievement de un usuario
     * 
     * @param userId ID del usuario
     * @param achievementCode Código del achievement a remover
     */
    void removeAchievement(Long userId, String achievementCode);
    
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
