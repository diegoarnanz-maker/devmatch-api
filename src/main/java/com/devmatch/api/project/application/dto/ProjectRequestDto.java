package com.devmatch.api.project.application.dto;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO para solicitudes de creación y actualización de proyectos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {
    
    @NotBlank(message = "El título del proyecto es obligatorio")
    private String title;
    
    @NotBlank(message = "La descripción del proyecto es obligatoria")
    private String description;
    
    @NotNull(message = "El estado del proyecto es obligatorio")
    private ProjectStatus status;
    
    private String repoUrl;
    
    private String coverImageUrl;
    
    private Integer estimatedDurationWeeks;
    
    private Integer maxTeamSize;
    
    private boolean isPublic = true;
    
    /**
     * Lista de nombres de tags/tecnologías para el proyecto
     * Ej: ["Java", "Spring Boot", "React"]
     */
    private List<String> tags;
} 