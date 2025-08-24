package com.devmatch.api.achievement.application.port.in;

import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Puerto de entrada para la gestión de achievements.
 * Define las operaciones de consulta de achievements (solo lectura).
 */
public interface AchievementManagementUseCase {
    
    /**
     * Obtiene un achievement por ID
     * 
     * @param id ID del achievement
     * @return DTO del achievement encontrado
     */
    AchievementResponseDto getAchievementById(Long id);
    
    /**
     * Obtiene un achievement por código
     * 
     * @param code Código del achievement
     * @return DTO del achievement encontrado
     */
    AchievementResponseDto getAchievementByCode(String code);
    
    /**
     * Obtiene todos los achievements activos
     * 
     * @return Lista de DTOs de achievements
     */
    List<AchievementResponseDto> getAllActiveAchievements();
    
    /**
     * Obtiene todos los achievements activos con paginación
     * 
     * @param pageable Parámetros de paginación y ordenación
     * @return Página de DTOs de achievements
     */
    Page<AchievementResponseDto> getAllActiveAchievementsPaginated(Pageable pageable);
    
    /**
     * Obtiene achievements por tipo
     * 
     * @param type Tipo de achievement
     * @return Lista de DTOs de achievements del tipo especificado
     */
    List<AchievementResponseDto> getAchievementsByType(String type);
}
