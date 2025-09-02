package com.devmatch.api.achievement.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para asignar achievements a usuarios por parte de administradores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserAchievementRequestDto {
    

    
    @NotNull(message = "El ID del achievement es obligatorio")
    private Long achievementId;
}
