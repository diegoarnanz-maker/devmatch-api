package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se actualiza un logro existente.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementUpdatedEvent extends BaseDomainEvent {
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final String achievementDescription;
    private final String achievementIcon;
    private final Long updatedBy;
    private final String changeReason;

    public AchievementUpdatedEvent(Long achievementId, String achievementCode, String achievementName, 
                                  String achievementDescription, String achievementIcon, 
                                  Long updatedBy, String changeReason) {
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.achievementIcon = achievementIcon;
        this.updatedBy = updatedBy;
        this.changeReason = changeReason;
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

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public String getAchievementIcon() {
        return achievementIcon;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }
}
