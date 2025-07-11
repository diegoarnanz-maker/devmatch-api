package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.project.domain.model.valueobject.ProjectTitle;
import com.devmatch.api.project.domain.model.valueobject.ProjectDescription;
import com.devmatch.api.project.domain.model.valueobject.RepositoryUrl;
import com.devmatch.api.project.domain.model.valueobject.CoverImageUrl;
import com.devmatch.api.project.domain.model.valueobject.ProjectDuration;
import com.devmatch.api.project.domain.model.valueobject.TeamSize;

public class Project {
    private final Long id;
    private final ProjectTitle title;
    private final ProjectDescription description;
    private final ProjectStatus status;
    private final Long ownerId;
    private final RepositoryUrl repoUrl;
    private final CoverImageUrl coverImageUrl;
    private final ProjectDuration estimatedDuration;
    private final TeamSize maxTeamSize;
    private final boolean isPublic;
    private final boolean isActive;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Constructor para crear nuevo proyecto
    public Project(ProjectTitle title, ProjectDescription description, ProjectStatus status, Long ownerId, 
                   RepositoryUrl repoUrl, CoverImageUrl coverImageUrl, ProjectDuration estimatedDuration, 
                   TeamSize maxTeamSize, boolean isPublic) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.repoUrl = repoUrl;
        this.coverImageUrl = coverImageUrl;
        this.estimatedDuration = estimatedDuration;
        this.maxTeamSize = maxTeamSize;
        this.isPublic = isPublic;
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    // Constructor para cargar proyecto existente
    public Project(Long id, ProjectTitle title, ProjectDescription description, ProjectStatus status, 
                   Long ownerId, RepositoryUrl repoUrl, CoverImageUrl coverImageUrl, 
                   ProjectDuration estimatedDuration, TeamSize maxTeamSize, 
                   boolean isPublic, boolean isActive, boolean isDeleted, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.repoUrl = repoUrl;
        this.coverImageUrl = coverImageUrl;
        this.estimatedDuration = estimatedDuration;
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

    public ProjectTitle getTitle() {
        return title;
    }

    public ProjectDescription getDescription() {
        return description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public RepositoryUrl getRepoUrl() {
        return repoUrl;
    }

    public CoverImageUrl getCoverImageUrl() {
        return coverImageUrl;
    }

    public ProjectDuration getEstimatedDuration() {
        return estimatedDuration;
    }

    public TeamSize getMaxTeamSize() {
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

    public boolean isFull(Integer currentTeamSize) {
        if (maxTeamSize == null || currentTeamSize == null) {
            return false;
        }
        return maxTeamSize.isFull(currentTeamSize);
    }

    public boolean isOwner(Long userId) {
        return this.ownerId.equals(userId);
    }

    /**
     * Verifica si el proyecto está en desarrollo activo
     * @return true si el proyecto está en estado OPEN, IN_PROGRESS o UNDER_REVIEW
     */
    public boolean isInActiveDevelopment() {
        return this.status == ProjectStatus.OPEN || 
               this.status == ProjectStatus.IN_PROGRESS || 
               this.status == ProjectStatus.UNDER_REVIEW;
    }

    public Project updateStatus(ProjectStatus newStatus) {
        return new Project(
            this.id, this.title, this.description, newStatus, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDuration,
            this.maxTeamSize, this.isPublic, this.isActive, this.isDeleted,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project updateVisibility(boolean isPublic) {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDuration,
            this.maxTeamSize, isPublic, this.isActive, this.isDeleted,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project deactivate() {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDuration,
            this.maxTeamSize, this.isPublic, false, this.isDeleted,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project softDelete() {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDuration,
            this.maxTeamSize, this.isPublic, false, true,
            this.createdAt, LocalDateTime.now()
        );
    }

    public Project restore() {
        return new Project(
            this.id, this.title, this.description, this.status, this.ownerId,
            this.repoUrl, this.coverImageUrl, this.estimatedDuration,
            this.maxTeamSize, this.isPublic, true, false,
            this.createdAt, LocalDateTime.now()
        );
    }
}
