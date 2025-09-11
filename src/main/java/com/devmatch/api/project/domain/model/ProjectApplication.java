package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.project.domain.model.valueobject.ApplicationStatus;
import com.devmatch.api.project.domain.model.valueobject.MotivationMessage;

/**
 * Entidad de dominio que representa una aplicación de un usuario a un proyecto.
 * 
 * <p>Esta entidad modela la relación entre un usuario y un proyecto cuando el usuario
 * solicita unirse al proyecto. Contiene toda la información necesaria para que el
 * propietario del proyecto pueda evaluar y decidir sobre la aplicación.</p>
 * 
 * <h3>Características principales:</h3>
 * <ul>
 *   <li><strong>Relación usuario-proyecto:</strong> Conecta un usuario específico con un proyecto</li>
 *   <li><strong>Estado de aplicación:</strong> Controla el flujo de la aplicación (PENDING, ACCEPTED, REJECTED, CANCELLED)</li>
 *   <li><strong>Mensaje de motivación:</strong> Permite al usuario explicar por qué quiere unirse</li>
 *   <li><strong>Seguimiento:</strong> Rastrea si el propietario ha visto la aplicación</li>
 *   <li><strong>Temporal:</strong> Registra fechas de envío y resolución</li>
 * </ul>
 * 
 * <h3>Estados de aplicación:</h3>
 * <ul>
 *   <li><strong>PENDING:</strong> Aplicación enviada, esperando respuesta</li>
 *   <li><strong>ACCEPTED:</strong> Aplicación aceptada por el propietario</li>
 *   <li><strong>REJECTED:</strong> Aplicación rechazada por el propietario</li>
 *   <li><strong>CANCELLED:</strong> Aplicación cancelada por el usuario</li>
 * </ul>
 * 
 * <h3>Reglas de negocio:</h3>
 * <ul>
 *   <li>Un usuario solo puede aplicar una vez por proyecto</li>
 *   <li>El propietario no puede aplicar a su propio proyecto</li>
 *   <li>Las aplicaciones expiran después de un tiempo determinado</li>
 *   <li>Una vez aceptada, la aplicación no puede ser cancelada</li>
 * </ul>
 * 
 * @see <a href="../../../../../docs/domain/project.md">Documentación completa del dominio</a>
 * @author diegoarnanz-maker
 * @version 1.0
 * @since 2025
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

    public boolean canBeExpired() {
        return isPending() && isActive && !isDeleted;
    }

    public ProjectApplication expire() {
        if (!canBeExpired()) {
            throw new IllegalStateException("La aplicación no puede ser expirada");
        }
        return new ProjectApplication(
            id, projectId, userId, motivationMessage,
            ApplicationStatus.EXPIRED, seenByOwner, submittedAt,
            LocalDateTime.now(), isActive, isDeleted,
            createdAt, LocalDateTime.now()
        );
    }

    public boolean isExpired() {
        return status == ApplicationStatus.EXPIRED;
    }
} 