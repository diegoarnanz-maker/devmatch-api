package com.devmatch.api.achievement.application.dto;

/**
 * DTO simple para mostrar el progreso de un achievement.
 * Usa solo campos que existen en la base de datos.
 */
public class AchievementProgressDto {
    
    private String achievementCode;
    private String achievementTitle;
    private String achievementDescription;
    private Integer currentProgress;
    private Integer requiredProgress;
    private String achievementType;
    
    // Constructor por defecto
    public AchievementProgressDto() {}
    
    // Constructor completo
    public AchievementProgressDto(String achievementCode, String achievementTitle,
                                 String achievementDescription, Integer currentProgress,
                                 Integer requiredProgress, String achievementType) {
        this.achievementCode = achievementCode;
        this.achievementTitle = achievementTitle;
        this.achievementDescription = achievementDescription;
        this.currentProgress = currentProgress;
        this.requiredProgress = requiredProgress;
        this.achievementType = achievementType;
    }
    
    // Getters y Setters
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
    
    public Integer getCurrentProgress() {
        return currentProgress;
    }
    
    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }
    
    public Integer getRequiredProgress() {
        return requiredProgress;
    }
    
    public void setRequiredProgress(Integer requiredProgress) {
        this.requiredProgress = requiredProgress;
    }
    
    public String getAchievementType() {
        return achievementType;
    }
    
    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }
    
    // Método de utilidad para calcular el porcentaje
    public Double getProgressPercentage() {
        if (requiredProgress == null || requiredProgress == 0) {
            return 0.0;
        }
        return (double) currentProgress / requiredProgress * 100;
    }
}
