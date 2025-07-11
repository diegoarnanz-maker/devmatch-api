package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.project.domain.model.valueobject.ApplicationStatus;
import com.devmatch.api.project.domain.model.valueobject.MotivationMessage;

/**
 * Entidad de dominio que representa una aplicación a un proyecto.
 * Basada en la tabla project_applications del DDL.
 */
public class ProjectApplication {
    private final Long id;
    private final Long projectId;
    private final Long userId;
    private final MotivationMessage motivationMessage;
    private final ApplicationStatus status;
    private final boolean seenByOwner;
    private final LocalDateTime submittedAt;
    private final LocalDateTime resolvedAt;
    private final boolean isActive;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Constructor para crear nueva aplicación
    public ProjectApplication(Long projectId, Long userId, MotivationMessage motivationMessage) {
        this.id = null;
        this.projectId = projectId;
        this.userId = userId;
        this.motivationMessage = motivationMessage;
        this.status = ApplicationStatus.PENDING;
        this.seenByOwner = false;
        this.submittedAt = LocalDateTime.now();
        this.resolvedAt = null;
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    // Constructor para cargar aplicación existente
    public ProjectApplication(Long id, Long projectId, Long userId, MotivationMessage motivationMessage,
                            ApplicationStatus status, boolean seenByOwner, LocalDateTime submittedAt,
                            LocalDateTime resolvedAt, boolean isActive, boolean isDeleted,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.motivationMessage = motivationMessage;
        this.status = status;
        this.seenByOwner = seenByOwner;
        this.submittedAt = submittedAt;
        this.resolvedAt = resolvedAt;
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

    public MotivationMessage getMotivationMessage() {
        return motivationMessage;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public boolean isSeenByOwner() {
        return seenByOwner;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
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
    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == ApplicationStatus.ACCEPTED;
    }

    public boolean isRejected() {
        return status == ApplicationStatus.REJECTED;
    }

    public boolean canBeAccepted() {
        return isPending() && isActive && !isDeleted;
    }

    public boolean canBeRejected() {
        return isPending() && isActive && !isDeleted;
    }

    public ProjectApplication accept() {
        if (!canBeAccepted()) {
            throw new IllegalStateException("La aplicación no puede ser aceptada");
        }
        return new ProjectApplication(
            id, projectId, userId, motivationMessage,
            ApplicationStatus.ACCEPTED, seenByOwner, submittedAt,
            LocalDateTime.now(), isActive, isDeleted,
            createdAt, LocalDateTime.now()
        );
    }

    public ProjectApplication reject() {
        if (!canBeRejected()) {
            throw new IllegalStateException("La aplicación no puede ser rechazada");
        }
        return new ProjectApplication(
            id, projectId, userId, motivationMessage,
            ApplicationStatus.REJECTED, seenByOwner, submittedAt,
            LocalDateTime.now(), isActive, isDeleted,
            createdAt, LocalDateTime.now()
        );
    }

    public ProjectApplication markAsSeen() {
        if (seenByOwner) {
            return this; // Ya está marcada como vista
        }
        return new ProjectApplication(
            id, projectId, userId, motivationMessage,
            status, true, submittedAt, resolvedAt,
            isActive, isDeleted, createdAt, LocalDateTime.now()
        );
    }

    public boolean canBeCancelled() {
        return isPending() && isActive && !isDeleted;
    }

    public ProjectApplication cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("La aplicación no puede ser cancelada");
        }
        return new ProjectApplication(
            id, projectId, userId, motivationMessage,
            status, seenByOwner, submittedAt, resolvedAt,
            false, isDeleted, createdAt, LocalDateTime.now()
        );
    }
} 