package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se elimina un achievement.
 * Contiene la información del achievement eliminado.
 */
public class AchievementDeletedEvent extends BaseDomainEvent {
    private final Long achievementId;    // ID del achievement eliminado
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final Long deletedBy;        // ID del usuario que eliminó el achievement
    private final String deletionReason; // Razón de la eliminación
    private final int affectedUsers;     // Número de usuarios afectados por la eliminación

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
