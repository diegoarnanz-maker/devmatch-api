package com.devmatch.api.achievement.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.achievement.domain.model.valueobject.AchievementCode;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementTitle;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementDescription;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementType;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementIcon;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementPoints;
import com.devmatch.api.shared.domain.model.BaseDomainEntity;

/**
 * Entidad de dominio que representa un logro en la plataforma DevMatch.
 * Corresponde a la tabla achievement_catalog del DDL.
 * Un logro es una meta o hito que los usuarios pueden alcanzar.
 */
public class Achievement extends BaseDomainEntity {
    
    private final AchievementCode code;
    private final AchievementTitle title;
    private final AchievementDescription description;
    private final AchievementPoints points;
    private final AchievementType type;
    private final AchievementIcon icon;
    
    // Constructor para crear nuevo logro
    public Achievement(AchievementCode code, AchievementTitle title, AchievementDescription description,
                      AchievementPoints points, AchievementType type, AchievementIcon icon) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.points = points;
        this.type = type;
        this.icon = icon;
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }
    
    // Constructor para cargar logro existente
    public Achievement(String code, String title, String description, Integer points, String type, String iconUrl,
                      boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = new AchievementCode(code);
        this.title = new AchievementTitle(title);
        this.description = new AchievementDescription(description);
        this.points = new AchievementPoints(points != null ? points : 10);
        this.type = new AchievementType(type != null ? type : "GENERAL");
        this.icon = new AchievementIcon(iconUrl != null ? iconUrl : "");
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Constructor para cargar logro existente con value objects
    public Achievement(AchievementCode code, AchievementTitle title, AchievementDescription description,
                      AchievementPoints points, AchievementType type, AchievementIcon icon,
                      boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.points = points;
        this.type = type;
        this.icon = icon;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public AchievementCode getCode() {
        return code;
    }
    
    public AchievementTitle getTitle() {
        return title;
    }
    
    public AchievementDescription getDescription() {
        return description;
    }
    
    public AchievementPoints getPoints() {
        return points;
    }
    
    public AchievementType getType() {
        return type;
    }
    
    public AchievementIcon getIcon() {
        return icon;
    }
    
    // Métodos de negocio
    public boolean isBeginnerFriendly() {
        return type.isBeginnerFriendly();
    }
    
    public boolean isAdvanced() {
        return type.isAdvanced();
    }
    
    public boolean isSocial() {
        return type.isSocial();
    }
    
    public boolean isTechnical() {
        return type.isTechnical();
    }
    
    public boolean isProfileRelated() {
        return type.isProfileRelated();
    }
    
    public boolean isProjectRelated() {
        return type.isProjectRelated();
    }
    
    public boolean isReviewRelated() {
        return type.isReviewRelated();
    }
    
    public boolean isLeadershipRelated() {
        return type.isLeadershipRelated();
    }
    
    public boolean isVeteranRelated() {
        return type.isVeteranRelated();
    }
    
    public boolean isLowPoints() {
        return points.isLow();
    }
    
    public boolean isMediumPoints() {
        return points.isMedium();
    }
    
    public boolean isHighPoints() {
        return points.isHigh();
    }
    
    public boolean isEpicPoints() {
        return points.isEpic();
    }
    
    public String getPointsTier() {
        return points.getTier();
    }
    
    public String getDisplayPoints() {
        return points.getDisplayValue();
    }
    
    public String getFullDisplayName() {
        return title.getValue() + " (" + points.getDisplayValue() + " pts)";
    }
    
    public boolean canBeUnlockedBy(Long userId) {
        // Lógica para determinar si un usuario puede desbloquear este logro
        // Por ejemplo, verificar requisitos previos, nivel del usuario, etc.
        return isActive && !isDeleted;
    }
    
    public boolean isRare() {
        // Un logro es raro si tiene muchos puntos o es de tipo veterano
        return points.isEpic() || type.isVeteranRelated();
    }
    
    public boolean isCommon() {
        // Un logro es común si tiene pocos puntos y es de tipo básico
        return points.isLow() && type.isBeginnerFriendly();
    }
    
    // Métodos para validar coherencia con el DDL
    public boolean isValidForDatabase() {
        return code != null && title != null && description != null && 
               points != null && type != null && icon != null;
    }
    
    public boolean matchesCatalogStructure() {
        // Verificar que coincida con la estructura de achievement_catalog
        return code.getValue().length() <= 50 && 
               title.getValue().length() <= 100 && 
               description.getValue().length() <= 65535 && // TEXT limit
               icon.getValue().length() <= 255;
    }
    
    @Override
    public String toString() {
        return String.format("Achievement{code='%s', title='%s', type='%s', points=%d, active=%s}",
                           code.getValue(), title.getValue(), type.getValue(), points.getValue(), isActive);
    }
}
