package com.devmatch.api.project.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar el cambio de visibilidad de un proyecto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVisibilityRequestDto {
    
    @NotNull(message = "La visibilidad pública del proyecto es obligatoria")
    private boolean isPublic;
} 