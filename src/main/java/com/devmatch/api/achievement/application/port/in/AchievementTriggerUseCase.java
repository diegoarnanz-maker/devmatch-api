package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;

import java.util.List;

/**
 * Puerto de entrada para activación automática de logros.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public interface AchievementTriggerUseCase {
    
    /**
     * Procesa un trigger de achievement basado en una acción del usuario.
     * Este método se ejecuta automáticamente cuando ocurre un evento del sistema.
     * 
     * @param request DTO con la información del trigger
     * @return Lista de achievements desbloqueados (puede ser vacía si no se cumplen criterios)
     */
    List<AchievementUnlockedResponseDto> processAchievementTrigger(AchievementTriggerRequestDto request);
    
    /**
     * Verifica si un usuario puede desbloquear achievements específicos.
     * Útil para verificar progreso sin desbloquear.
     * 
     * @param userId ID del usuario
     * @param triggerType Tipo de trigger a verificar
     * @return Lista de achievements que podrían desbloquearse
     */
    List<String> checkPotentialAchievements(Long userId, String triggerType);
    
    /**
     * Obtiene el progreso de un usuario hacia achievements específicos.
     * 
     * @param userId ID del usuario
     * @param achievementType Tipo de achievement
     * @return Porcentaje de progreso (0-100)
     */
    int getUserProgressTowardsAchievement(Long userId, String achievementType);
    
    /**
     * Fuerza la verificación de todos los achievements para un usuario.
     * Útil para admin o para sincronización.
     * 
     * @param userId ID del usuario
     * @return Lista de achievements desbloqueados
     */
    List<AchievementUnlockedResponseDto> forceAchievementCheck(Long userId);
}
