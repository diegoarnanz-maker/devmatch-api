package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario desbloquea un achievement.
 * Contiene la información del achievement desbloqueado y el usuario.
 */
public class AchievementUnlockedEvent extends BaseDomainEvent {
    private final Long userId;           // ID del usuario que desbloqueó el achievement
    private final Long achievementId;    // ID del achievement desbloqueado
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final String achievementDescription; // Descripción del achievement

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
