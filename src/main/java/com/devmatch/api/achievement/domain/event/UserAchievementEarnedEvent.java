package com.devmatch.api.achievement.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario gana un achievement.
 * Contiene la información del achievement ganado y el usuario.
 */
public class UserAchievementEarnedEvent extends BaseDomainEvent {
    private final Long userId;           // ID del usuario que ganó el achievement
    private final Long userAchievementId; // ID del user achievement
    private final Long achievementId;    // ID del achievement
    private final String achievementCode; // Código del achievement
    private final String achievementName; // Nombre del achievement
    private final String achievementDescription; // Descripción del achievement
    private final String achievementIcon; // Icono del achievement
    private final String earnedAt;       // Fecha/hora cuando se ganó

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
