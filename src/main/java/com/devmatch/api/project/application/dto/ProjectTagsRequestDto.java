package com.devmatch.api.project.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * DTO de solicitud para gestión de tags de proyecto.
 * 
 * @author diegoarnanz-maker
 * @since 2025
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