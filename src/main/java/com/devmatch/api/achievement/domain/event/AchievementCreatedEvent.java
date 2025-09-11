package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se crea un nuevo logro.
 * Contiene información del logro creado y del usuario que lo creó.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementCreatedEvent extends BaseDomainEvent {
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final String achievementDescription;
    private final String achievementIcon;
    private final Long createdBy;

    public AchievementCreatedEvent(Long achievementId, String achievementCode, String achievementName, 
                                  String achievementDescription, String achievementIcon, Long createdBy) {
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.achievementIcon = achievementIcon;
        this.createdBy = createdBy;
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

    public Long getCreatedBy() {
        return createdBy;
    }
}
