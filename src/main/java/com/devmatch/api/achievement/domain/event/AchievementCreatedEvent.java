package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se crea un nuevo achievement.
 * Contiene la información del achievement creado.
 */
public class AchievementCreatedEvent extends BaseDomainEvent {
    private final Long achievementId;    // ID del achievement creado
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final String achievementDescription; // Descripción del achievement
    private final String achievementIcon; // Icono del achievement
    private final Long createdBy;        // ID del usuario que creó el achievement

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
