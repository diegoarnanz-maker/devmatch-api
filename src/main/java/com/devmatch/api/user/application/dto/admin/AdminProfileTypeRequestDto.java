package com.devmatch.api.user.application.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminProfileTypeRequestDto {
    
    @NotBlank(message = "El nombre del tipo de perfil es requerido")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;
} 