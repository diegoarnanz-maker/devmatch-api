package com.devmatch.api.project.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para cambio de visibilidad de proyecto.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVisibilityRequestDto {
    
    @NotNull(message = "La visibilidad pública del proyecto es obligatoria")
    @JsonProperty("isPublic")
    private boolean isPublic;
} 