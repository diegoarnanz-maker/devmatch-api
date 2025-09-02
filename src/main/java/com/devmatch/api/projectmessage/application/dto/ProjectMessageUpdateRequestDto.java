package com.devmatch.api.projectmessage.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el contenido de un mensaje existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMessageUpdateRequestDto {
    
    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 2000, message = "El contenido debe tener entre 1 y 2000 caracteres")
    private String content;
}
