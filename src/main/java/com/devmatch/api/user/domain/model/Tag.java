package com.devmatch.api.user.domain.model;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Modelo de dominio para Tag.
 * Representa una etiqueta que puede ser asignada a usuarios.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseDomainEntity {
    
    private String name;
    private String tagType;
    
    public Tag() {
        super();
    }
    
    public Tag(String name, String tagType) {
        super();
        this.name = name;
        this.tagType = tagType;
    }
    
    public void setActive(boolean active) {
        super.isActive = active;
    }
} 