package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando hay progreso hacia un logro.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementProgressEvent extends BaseDomainEvent {
    private final Long userId;
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final int currentProgress;
    private final int requiredProgress;
    private final double progressPercentage;
    private final String progressType;

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
