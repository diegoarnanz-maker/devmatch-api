package com.devmatch.api.user.application.dto.profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileTypeRequestDto {
    
    @NotNull(message = "El ID del tipo de perfil es requerido")
    private Long profileTypeId;
} 