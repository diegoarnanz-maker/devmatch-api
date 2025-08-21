package com.devmatch.api.achievement.application.dto;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de user achievements.
 * Contiene la información de un achievement desbloqueado por un usuario.
 */
public class UserAchievementResponseDto {
    
    private Long id;
    private Long userId;
    private String achievementCode;
    private String achievementTitle;
    private String achievementDescription;
    private Integer achievementPoints;
    private String achievementType;
    private String achievementIcon;
    private LocalDateTime achievedAt;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructores
    public UserAchievementResponseDto() {}
    
    public UserAchievementResponseDto(Long id, Long userId, String achievementCode, String achievementTitle,
                                     String achievementDescription, Integer achievementPoints, String achievementType,
                                     String achievementIcon, LocalDateTime achievedAt, boolean isActive,
                                     boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.achievementCode = achievementCode;
        this.achievementTitle = achievementTitle;
        this.achievementDescription = achievementDescription;
        this.achievementPoints = achievementPoints;
        this.achievementType = achievementType;
        this.achievementIcon = achievementIcon;
        this.achievedAt = achievedAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }
    
    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
