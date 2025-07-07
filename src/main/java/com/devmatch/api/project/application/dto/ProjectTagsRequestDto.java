package com.devmatch.api.project.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * DTO para gestionar tags de un proyecto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTagsRequestDto {
    
    /**
     * Lista de nombres de tags a agregar al proyecto
     * Ej: ["Java", "Spring Boot", "React"]
     */
    private List<String> tagNames;
} 