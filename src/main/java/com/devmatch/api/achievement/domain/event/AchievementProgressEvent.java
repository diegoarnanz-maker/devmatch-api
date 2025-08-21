package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando hay progreso hacia un achievement.
 * Contiene la información del progreso del usuario hacia el achievement.
 */
public class AchievementProgressEvent extends BaseDomainEvent {
    private final Long userId;           // ID del usuario
    private final Long achievementId;    // ID del achievement
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final int currentProgress;   // Progreso actual
    private final int requiredProgress;  // Progreso requerido
    private final double progressPercentage; // Porcentaje de progreso
    private final String progressType;   // Tipo de progreso (ej: "projects_joined", "reviews_given")

    public AchievementProgressEvent(Long userId, Long achievementId, String achievementCode, 
                                   String achievementName, int currentProgress, int requiredProgress, 
                                   double progressPercentage, String progressType) {
        this.userId = userId;
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.currentProgress = currentProgress;
        this.requiredProgress = requiredProgress;
        this.progressPercentage = progressPercentage;
        this.progressType = progressType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public String getAchievementCode() {
        return achievementCode;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getRequiredProgress() {
        return requiredProgress;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public String getProgressType() {
        return progressType;
    }
}
