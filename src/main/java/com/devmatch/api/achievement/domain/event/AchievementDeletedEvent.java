package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se elimina un logro del sistema.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementDeletedEvent extends BaseDomainEvent {
    private final Long achievementId;
    private final String achievementCode;
    private final String achievementName;
    private final Long deletedBy;
    private final String deletionReason;
    private final int affectedUsers;

    public AchievementDeletedEvent(Long achievementId, String achievementCode, String achievementName, 
                                  Long deletedBy, String deletionReason, int affectedUsers) {
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.achievementName = achievementName;
        this.deletedBy = deletedBy;
        this.deletionReason = deletionReason;
        this.affectedUsers = affectedUsers;
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

    public Long getDeletedBy() {
        return deletedBy;
    }

    public String getDeletionReason() {
        return deletionReason;
    }

    public int getAffectedUsers() {
        return affectedUsers;
    }
}
