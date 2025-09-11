package com.devmatch.api.achievement.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para asignar logros a usuarios.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserAchievementRequestDto {
    

    
    @NotNull(message = "El ID del achievement es obligatorio")
    private Long achievementId;
}
