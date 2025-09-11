package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.project.domain.model.valueobject.ProjectTitle;
import com.devmatch.api.project.domain.model.valueobject.ProjectDescription;
import com.devmatch.api.project.domain.model.valueobject.RepositoryUrl;
import com.devmatch.api.project.domain.model.valueobject.CoverImageUrl;
import com.devmatch.api.project.domain.model.valueobject.ProjectDuration;
import com.devmatch.api.project.domain.model.valueobject.TeamSize;

/**
 * Entidad principal del dominio que representa un proyecto en el sistema.
 * 
 * <p>Un proyecto es la unidad central de trabajo en DevMatch, donde los desarrolladores
 * pueden colaborar, compartir conocimientos y construir soluciones juntos. Esta entidad
 * encapsula toda la información y comportamiento relacionado con un proyecto específico.</p>
 * 
 * <h3>Características principales:</h3>
 * <ul>
 *   <li><strong>Identidad única:</strong> Cada proyecto tiene un ID único e inmutable</li>
 *   <li><strong>Propietario:</strong> Un usuario que crea y gestiona el proyecto</li>
 *   <li><strong>Estado:</strong> Controla el ciclo de vida del proyecto (DRAFT, ACTIVE, COMPLETED, CANCELLED)</li>
 *   <li><strong>Visibilidad:</strong> Puede ser público o privado</li>
 *   <li><strong>Colaboración:</strong> Permite que otros usuarios se unan como miembros</li>
 * </ul>
 * 
 * <h3>Value Objects utilizados:</h3>
 * <ul>
 *   <li><strong>ProjectTitle:</strong> Título del proyecto con validaciones</li>
 *   <li><strong>ProjectDescription:</strong> Descripción detallada del proyecto</li>
 *   <li><strong>ProjectStatus:</strong> Estado actual del proyecto</li>
 *   <li><strong>RepositoryUrl:</strong> URL del repositorio de código</li>
 *   <li><strong>CoverImageUrl:</strong> URL de la imagen de portada</li>
 *   <li><strong>ProjectDuration:</strong> Duración estimada del proyecto</li>
 *   <li><strong>TeamSize:</strong> Tamaño máximo del equipo</li>
 * </ul>
 * 
 * <h3>Reglas de negocio:</h3>
 * <ul>
 *   <li>Un proyecto debe tener un propietario válido</li>
 *   <li>El estado del proyecto determina qué operaciones son permitidas</li>
 *   <li>Los proyectos públicos son visibles para todos los usuarios</li>
 *   <li>Los proyectos privados solo son visibles para miembros</li>
 *   <li>Un proyecto no puede ser eliminado si tiene miembros activos</li>
 * </ul>
 * 
 * <h3>Ejemplos de uso:</h3>
 * <pre>{@code
 * // Crear un nuevo proyecto
 * Project project = new Project(
 *     new ProjectTitle("Mi Proyecto Web"),
 *     new ProjectDescription("Una aplicación web moderna"),
 *     ProjectStatus.DRAFT,
 *     userId,
 *     new RepositoryUrl("https://github.com/user/project"),
 *     new CoverImageUrl("https://example.com/image.jpg"),
 *     new ProjectDuration(30),
 *     new TeamSize(5),
 *     true
 * );
 * 
 * // Verificar si el proyecto está activo
 * boolean isActive = project.isActive();
 * 
 * // Obtener información del proyecto
 * String title = project.getTitle().getValue();
 * ProjectStatus status = project.getStatus();
 * }</pre>
 * 
 * @see <a href="../../../../../docs/domain/project.md">Documentación completa del dominio</a>
 * @author diegoarnanz-maker
 * @version 1.0
 * @since 2025
 */
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
        return (this.isPublic || this.ownerId.equals(userId)) && this.isActive && !this.isDeleted;
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
