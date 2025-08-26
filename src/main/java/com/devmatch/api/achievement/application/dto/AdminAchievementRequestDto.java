package com.devmatch.api.achievement.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear y actualizar achievements por parte de administradores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAchievementRequestDto {
    
    @NotBlank(message = "El código del achievement es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String code;
    
    @NotBlank(message = "El título del achievement es obligatorio")
    @Size(max = 100, message = "El título no puede exceder 100 caracteres")
    private String title;
    
    @NotBlank(message = "La descripción del achievement es obligatoria")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @NotNull(message = "Los puntos del achievement son obligatorios")
    @Positive(message = "Los puntos deben ser positivos")
    private Integer points;
    
    @NotBlank(message = "El tipo del achievement es obligatorio")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String type;
    
    @Size(max = 255, message = "La URL del icono no puede exceder 255 caracteres")
    private String iconUrl;
    
    private Boolean isActive = true;
}
