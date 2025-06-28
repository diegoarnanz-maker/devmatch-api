package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un miembro de un proyecto.
 * Basada en la tabla project_members del DDL.
 */
public class ProjectMember {
    private final Long id;
    private final Long projectId;
    private final Long userId;
    private final String memberRole;
    private final boolean isOwner;
    private final LocalDateTime joinedAt;
    private final LocalDateTime leftAt;
    private final boolean isActive;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Constructor para crear nuevo miembro
    public ProjectMember(Long projectId, Long userId, String memberRole, boolean isOwner) {
        this.id = null;
        this.projectId = projectId;
        this.userId = userId;
        this.memberRole = memberRole;
        this.isOwner = isOwner;
        this.joinedAt = LocalDateTime.now();
        this.leftAt = null;
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    // Constructor para cargar miembro existente
    public ProjectMember(Long id, Long projectId, Long userId, String memberRole,
                        boolean isOwner, LocalDateTime joinedAt, LocalDateTime leftAt,
                        boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.memberRole = memberRole;
        this.isOwner = isOwner;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
} 