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
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
    
    @NotNull(message = "El código del achievement es obligatorio")
    private String achievementCode;
    
    /**
     * Si es true, se asigna el achievement. Si es false, se remueve.
     */
    private Boolean assign = true;
}
