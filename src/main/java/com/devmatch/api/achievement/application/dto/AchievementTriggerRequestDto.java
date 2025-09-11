package com.devmatch.api.achievement.application.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de solicitud para activación automática de logros.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementTriggerRequestDto {
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
    
    @NotNull(message = "El tipo de achievement es obligatorio")
    private String achievementType;
    
    // Constructor por defecto
    public AchievementTriggerRequestDto() {}
    
    // Constructor simple
    public AchievementTriggerRequestDto(Long userId, String achievementType) {
        this.userId = userId;
        this.achievementType = achievementType;
    }
    
    // Getters y Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getAchievementType() {
        return achievementType;
    }
    
    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }
}
