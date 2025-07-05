package com.devmatch.api.tag.domain.model;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo de dominio para Tag.
 * Representa una etiqueta que puede ser asignada a usuarios y proyectos.
 */
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
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getTagType() {
        return tagType;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }
    
    public void setTagType(String tagType) {
        this.tagType = tagType;
        updateTimestamp();
    }
    
    public void setActive(boolean active) {
        super.isActive = active;
        updateTimestamp();
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setDeleted(boolean deleted) {
        super.isDeleted = deleted;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        super.createdAt = createdAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        super.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tagType='" + tagType + '\'' +
                ", isActive=" + isActive +
                '}';
    }
} 