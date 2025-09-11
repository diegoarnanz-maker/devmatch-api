package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.AdminAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



/**
 * Puerto de entrada para gestión administrativa de logros.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public interface AdminAchievementUseCase {
    
    /**
     * Crea un nuevo achievement
     * 
     * @param request DTO con los datos del achievement a crear
     * @return Achievement creado
     */
    AchievementResponseDto createAchievement(AdminAchievementRequestDto request);
    
    /**
     * Actualiza un achievement existente
     * 
     * @param achievementId ID del achievement a actualizar
     * @param request DTO con los datos actualizados
     * @return Achievement actualizado
     */
    AchievementResponseDto updateAchievement(Long achievementId, AdminAchievementRequestDto request);
    
    /**
     * Elimina (soft delete) un achievement
     * 
     * @param achievementId ID del achievement a eliminar
     */
    void deleteAchievement(Long achievementId);
    
    /**
     * Activa/desactiva un achievement
     * 
     * @param achievementId ID del achievement
     * @return Achievement con estado actualizado
     */
    AchievementResponseDto toggleAchievementStatus(Long achievementId);
    
    /**
     * Obtiene un achievement por ID (incluyendo inactivos)
     * 
     * @param achievementId ID del achievement
     * @return Achievement encontrado
     */
    AchievementResponseDto getAchievementById(Long achievementId);
    
    /**
     * Obtiene todos los achievements (incluyendo inactivos) con paginación
     * 
     * @param pageable Parámetros de paginación
     * @return Página de achievements
     */
    Page<AchievementResponseDto> getAllAchievementsPaginated(Pageable pageable);
    
    /**
     * Obtiene achievements por tipo (incluyendo inactivos y eliminados) con paginación
     * 
     * @param type Tipo de achievement
     * @param pageable Parámetros de paginación
     * @return Página de achievements del tipo especificado
     */
    Page<AchievementResponseDto> getAchievementsByTypePaginated(String type, Pageable pageable);
    
    /**
     * Obtiene un achievement por código (incluyendo inactivos)
     * 
     * @param code Código del achievement
     * @return Achievement encontrado
     */
    AchievementResponseDto getAchievementByCode(String code);
}
