package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;

public class Project {
    private final Long id;
    private final String title;
    private final String description;
    private final ProjectStatus status;
    private final Long ownerId;
    private final String repoUrl;
    private final String coverImageUrl;
    private final Integer estimatedDurationWeeks;
    private final Integer maxTeamSize;
    private final boolean isPublic;
    private final boolean isActive;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Constructor para crear nuevo proyecto
    public Project(String title, String description, ProjectStatus status, Long ownerId, 
                   String repoUrl, String coverImageUrl, Integer estimatedDurationWeeks, 
                   Integer maxTeamSize, boolean isPublic) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.repoUrl = repoUrl;
        this.coverImageUrl = coverImageUrl;
        this.estimatedDurationWeeks = estimatedDurationWeeks;
        this.maxTeamSize = maxTeamSize;
        this.isPublic = isPublic;
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    // Constructor para cargar proyecto existente
    public Project(Long id, String title, String description, ProjectStatus status, 
                   Long ownerId, String repoUrl, String coverImageUrl, 
                   Integer estimatedDurationWeeks, Integer maxTeamSize, 
                   boolean isPublic, boolean isActive, boolean isDeleted, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.repoUrl = repoUrl;
        this.coverImageUrl = coverImageUrl;
        this.estimatedDurationWeeks = estimatedDurationWeeks;
        this.maxTeamSize = maxTeamSize;
        this.isPublic = isPublic;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public Integer getEstimatedDurationWeeks() {
        return estimatedDurationWeeks;
    }

    public Integer getMaxTeamSize() {
        return maxTeamSize;
    }

    public boolean isPublic() {
        return isPublic;
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

    // Métodos de negocio
    public boolean canBeEditedBy(Long userId) {
        return this.ownerId.equals(userId) && this.isActive && !this.isDeleted;
    }

    public boolean isVisibleTo(Long userId) {
        return this.isPublic || this.ownerId.equals(userId);
    }

    public boolean isOpenForApplications() {
        return this.status == ProjectStatus.OPEN && this.isActive && !this.isDeleted;
    }

    public Project updateStatus(ProjectStatus newStatus) {
        return new Project(
            this.id, this.title, this.description, newStatus, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDurationWeeks,
            this.maxTeamSize, this.isPublic, this.isActive, this.isDeleted,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project deactivate() {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDurationWeeks,
            this.maxTeamSize, this.isPublic, false, this.isDeleted,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project softDelete() {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDurationWeeks,
            this.maxTeamSize, this.isPublic, false, true,
            this.createdAt, LocalDateTime.now()
        );
    }
}
