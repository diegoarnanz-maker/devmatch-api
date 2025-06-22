package com.devmatch.api.user.application.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserChangeEmailRequestDto {
    
    @NotBlank(message = "El email actual es obligatorio")
    @Email(message = "El formato del email actual no es válido")
    private String currentEmail;
    
    @NotBlank(message = "El nuevo email es obligatorio")
    @Email(message = "El formato del nuevo email no es válido")
    private String newEmail;
    
    @NotBlank(message = "La contraseña es obligatoria para cambiar el email")
    private String password;
} 