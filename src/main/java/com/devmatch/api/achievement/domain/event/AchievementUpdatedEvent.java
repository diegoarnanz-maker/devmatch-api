package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se actualiza un achievement.
 * Contiene la información del achievement actualizado.
 */
public class AchievementUpdatedEvent extends BaseDomainEvent {
    private final Long achievementId;    // ID del achievement actualizado
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final String achievementDescription; // Descripción del achievement
    private final String achievementIcon; // Icono del achievement
    private final Long updatedBy;        // ID del usuario que actualizó el achievement
    private final String changeReason;   // Razón del cambio

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
