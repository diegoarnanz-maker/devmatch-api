package com.devmatch.api.projectreview.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del propietario a una reseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseRequestDto {
    
    @NotBlank(message = "El mensaje de respuesta no puede estar vacío")
    @Size(max = 1000, message = "El mensaje de respuesta no puede exceder los 1000 caracteres")
    private String responseMessage;
    
    private Boolean isPublic = true; // Por defecto la respuesta es pública
}
