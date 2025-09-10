package com.devmatch.api.achievement.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.achievement.domain.model.valueobject.AchievementCode;
import com.devmatch.api.shared.domain.model.BaseDomainEntity;

/**
 * Entidad de dominio que representa un logro desbloqueado por un usuario.
 * 
 * <p>Mantiene la relación entre un usuario y un logro específico.
 * Corresponde a la tabla user_achievements del DDL.</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>Relación usuario-logro</li>
 *   <li>Timestamp de desbloqueo</li>
 *   <li>Estado activo/inactivo</li>
 * </ul>
 * 
 * @see <a href="../../../../docs/domain/achievement.md">Documentación completa del dominio</a>
 * @author DevMatch Team
 * @version 1.0
 * @since 2024
 */
public class UserAchievement extends BaseDomainEntity {
    
    private final Long userId;
    private final AchievementCode achievementCode;
    private final LocalDateTime achievedAt;
    
    // Constructor para crear nuevo logro desbloqueado
    public UserAchievement(Long userId, AchievementCode achievementCode) {
        this.userId = userId;
        this.achievementCode = achievementCode;
        this.achievedAt = LocalDateTime.now();
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }
    
    // Constructor para cargar logro desbloqueado existente
    public UserAchievement(Long id, Long userId, String achievementCode, LocalDateTime achievedAt,
                          boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.achievementCode = new AchievementCode(achievementCode);
        this.achievedAt = achievedAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Constructor para cargar logro desbloqueado existente con value objects
    public UserAchievement(Long id, Long userId, AchievementCode achievementCode, LocalDateTime achievedAt,
                          boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.achievementCode = achievementCode;
        this.achievedAt = achievedAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public Long getUserId() {
        return userId;
    }
    
    public AchievementCode getAchievementCode() {
        return achievementCode;
    }
    
    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }
    
    // Métodos de negocio
    public boolean isRecentlyAchieved() {
        // Un logro es reciente si se desbloqueó en las últimas 24 horas
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return achievedAt.isAfter(oneDayAgo);
    }
    
    public boolean isAchievedToday() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return achievedAt.isAfter(today) && achievedAt.isBefore(tomorrow);
    }
    
    public boolean isAchievedThisWeek() {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        return achievedAt.isAfter(weekAgo);
    }
    
    public boolean isAchievedThisMonth() {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return achievedAt.isAfter(monthAgo);
    }
    
    public long getDaysSinceAchievement() {
        return java.time.Duration.between(achievedAt, LocalDateTime.now()).toDays();
    }
    
    public long getHoursSinceAchievement() {
        return java.time.Duration.between(achievedAt, LocalDateTime.now()).toHours();
    }
    
    public String getTimeAgoDisplay() {
        long days = getDaysSinceAchievement();
        if (days == 0) {
            long hours = getHoursSinceAchievement();
            if (hours == 0) {
                return "Hace menos de 1 hora";
            } else if (hours == 1) {
                return "Hace 1 hora";
            } else {
                return "Hace " + hours + " horas";
            }
        } else if (days == 1) {
            return "Hace 1 día";
        } else if (days < 7) {
            return "Hace " + days + " días";
        } else if (days < 30) {
            long weeks = days / 7;
            return "Hace " + weeks + " semana" + (weeks > 1 ? "s" : "");
        } else {
            long months = days / 30;
            return "Hace " + months + " mes" + (months > 1 ? "es" : "");
        }
    }
    
    public boolean canBeDisplayed() {
        // Un logro puede mostrarse si está activo y no eliminado
        return isActive && !isDeleted;
    }
    
    public boolean isMilestone() {
        // Un logro es un hito si se desbloqueó en una fecha especial
        // Por ejemplo, primer día del mes, año nuevo, etc.
        return achievedAt.getDayOfMonth() == 1 || 
               (achievedAt.getMonthValue() == 1 && achievedAt.getDayOfMonth() == 1);
    }
    
    // Métodos para validar coherencia con el DDL
    public boolean isValidForDatabase() {
        return userId != null && achievementCode != null && achievedAt != null;
    }
    
    public boolean matchesUserAchievementStructure() {
        // Verificar que coincida con la estructura de user_achievements
        return userId > 0 && 
               achievementCode.getValue().length() <= 50;
    }
    
    @Override
    public String toString() {
        return String.format("UserAchievement{id=%d, userId=%d, achievementCode='%s', achievedAt=%s, active=%s}",
                           id, userId, achievementCode.getValue(), achievedAt, isActive);
    }
}
