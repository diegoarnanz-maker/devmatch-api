package com.devmatch.api.project.application.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO para filtrar proyectos públicos.
 * Todos los campos son opcionales, permitiendo filtros flexibles.
 */
@Data
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