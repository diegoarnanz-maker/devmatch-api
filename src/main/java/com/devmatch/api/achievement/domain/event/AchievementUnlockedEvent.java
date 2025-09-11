package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario desbloquea un logro.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementUnlockedEvent extends BaseDomainEvent {
    private final Long userId;
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final String achievementDescription;

    public AchievementUnlockedEvent(Long userId, Long achievementId, String achievementCode, 
                                   String achievementName, String achievementDescription) {
        this.userId = userId;
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
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

    public String getAchievementDescription() {
        return achievementDescription;
    }
}
