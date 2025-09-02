package com.devmatch.api.projectmessage.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo mensaje en un proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMessageRequestDto {
    
    @NotNull(message = "El ID del proyecto es obligatorio")
    private Long projectId;
    
    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 2000, message = "El contenido debe tener entre 1 y 2000 caracteres")
    private String content;
    
    @NotBlank(message = "El tipo de mensaje es obligatorio")
    private String messageType;
    
    private Long replyToMessageId; // Opcional, para respuestas
}
