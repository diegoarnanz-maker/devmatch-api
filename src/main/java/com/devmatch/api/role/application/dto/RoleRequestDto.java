package com.devmatch.api.role.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de creación de roles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDto {
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre del rol debe tener entre 2 y 50 caracteres")
    private String name;
    
    @NotBlank(message = "La descripción del rol es obligatoria")
    @Size(min = 5, max = 200, message = "La descripción del rol debe tener entre 5 y 200 caracteres")
    private String description;
} 