package com.devmatch.api.achievement.application.dto;

import java.time.LocalDateTime;

/**
 * DTO simple para achievements desbloqueados automáticamente.
 * Usa solo campos que existen en la base de datos.
 */
public class AchievementUnlockedResponseDto {
    
    private Long achievementId;
    private String achievementCode;
    private String achievementTitle;
    private String achievementDescription;
    private Integer achievementPoints;
    private String achievementType;
    private String achievementIcon;
    private Long userId;
    private LocalDateTime achievedAt;
    
    // Constructor por defecto
    public AchievementUnlockedResponseDto() {}
    
    // Constructor completo
    public AchievementUnlockedResponseDto(
            Long achievementId, String achievementCode, String achievementTitle,
            String achievementDescription, Integer achievementPoints, String achievementType,
            String achievementIcon, Long userId, LocalDateTime achievedAt) {
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementTitle = achievementTitle;
        this.achievementDescription = achievementDescription;
        this.achievementPoints = achievementPoints;
        this.achievementType = achievementType;
        this.achievementIcon = achievementIcon;
        this.userId = userId;
        this.achievedAt = achievedAt;
    }
    
    // Getters y Setters
    public Long getAchievementId() {
        return achievementId;
    }
    
    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }
    
    public String getAchievementCode() {
        return achievementCode;
    }
    
    public void setAchievementCode(String achievementCode) {
        this.achievementCode = achievementCode;
    }
    
    public String getAchievementTitle() {
        return achievementTitle;
    }
    
    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }
    
    public String getAchievementDescription() {
        return achievementDescription;
    }
    
    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }
    
    public Integer getAchievementPoints() {
        return achievementPoints;
    }
    
    public void setAchievementPoints(Integer achievementPoints) {
        this.achievementPoints = achievementPoints;
    }
    
    public String getAchievementType() {
        return achievementType;
    }
    
    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }
    
    public String getAchievementIcon() {
        return achievementIcon;
    }
    
    public void setAchievementIcon(String achievementIcon) {
        this.achievementIcon = achievementIcon;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }
    
    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }
}
