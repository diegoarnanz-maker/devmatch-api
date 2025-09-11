package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario gana un logro.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class UserAchievementEarnedEvent extends BaseDomainEvent {
    private final Long userId;
    private final Long userAchievementId;
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final String achievementDescription;
    private final String achievementIcon;
    private final String earnedAt;

    public UserAchievementEarnedEvent(Long userId, Long userAchievementId, Long achievementId, 
                                     String achievementCode, String achievementName, 
                                     String achievementDescription, String achievementIcon, String earnedAt) {
        this.userId = userId;
        this.userAchievementId = userAchievementId;
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.achievementIcon = achievementIcon;
        this.earnedAt = earnedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getUserAchievementId() {
        return userAchievementId;
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

    public String getEarnedAt() {
        return earnedAt;
    }
}
