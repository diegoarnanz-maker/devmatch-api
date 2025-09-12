package com.devmatch.api.project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO de solicitud para búsqueda de proyectos públicos.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPublicSearchRequestDto {
    
    /**
     * Título del proyecto (búsqueda parcial)
     */
    private String title;
    
    /**
     * Estado del proyecto (ACTIVE, ON_HOLD, COMPLETED, etc.)
     */
    private String status;
    
    /**
     * IDs de tags para filtrar proyectos que tengan estos tags
     */
    private List<Long> tagIds;
    
    /**
     * Solo proyectos activos (true) o incluir inactivos (false)
     */
    private Boolean isActive;
    
    /**
     * Tamaño mínimo del equipo requerido
     */
    private Integer minTeamSize;
    
    /**
     * Tamaño máximo del equipo requerido
     */
    private Integer maxTeamSize;
    
    /**
     * Duración mínima estimada en semanas
     */
    private Integer minDurationWeeks;
    
    /**
     * Duración máxima estimada en semanas
     */
    private Integer maxDurationWeeks;
} 