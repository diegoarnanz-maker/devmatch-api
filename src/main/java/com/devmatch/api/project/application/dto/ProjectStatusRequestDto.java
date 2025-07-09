package com.devmatch.api.project.application.dto;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar el cambio de estado de un proyecto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatusRequestDto {
    
    @NotNull(message = "El estado del proyecto es obligatorio")
    private ProjectStatus status;
} 