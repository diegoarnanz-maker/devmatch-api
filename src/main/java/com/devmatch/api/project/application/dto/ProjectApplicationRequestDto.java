package com.devmatch.api.project.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar una aplicación a un proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectApplicationRequestDto {

    @NotBlank(message = "El mensaje de motivación es obligatorio")
    @Size(min = 10, max = 1000, message = "El mensaje de motivación debe tener entre 10 y 1000 caracteres")
    private String motivationMessage;
} 