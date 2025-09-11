package com.devmatch.api.project.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un miembro de un proyecto.
 * 
 * <p>Esta entidad modela la relación entre un usuario y un proyecto cuando el usuario
 * es miembro activo del proyecto. Contiene información sobre el rol, estado y
 * fechas de participación del miembro en el proyecto.</p>
 * 
 * <h3>Características principales:</h3>
 * <ul>
 *   <li><strong>Relación usuario-proyecto:</strong> Conecta un usuario específico con un proyecto</li>
 *   <li><strong>Rol del miembro:</strong> Define el tipo de participación (DEVELOPER, DESIGNER, etc.)</li>
 *   <li><strong>Propietario:</strong> Identifica si el miembro es el propietario del proyecto</li>
 *   <li><strong>Estado activo:</strong> Controla si el miembro está actualmente en el proyecto</li>
 *   <li><strong>Temporal:</strong> Registra fechas de unión y salida</li>
 * </ul>
 * 
 * <h3>Roles de miembro:</h3>
 * <ul>
 *   <li><strong>OWNER:</strong> Propietario del proyecto</li>
 *   <li><strong>DEVELOPER:</strong> Desarrollador del proyecto</li>
 *   <li><strong>DESIGNER:</strong> Diseñador del proyecto</li>
 *   <li><strong>MANAGER:</strong> Gestor del proyecto</li>
 * </ul>
 * 
 * <h3>Reglas de negocio:</h3>
 * <ul>
 *   <li>Solo puede haber un propietario por proyecto</li>
 *   <li>Un usuario no puede ser miembro del mismo proyecto dos veces</li>
 *   <li>El propietario no puede ser eliminado del proyecto</li>
 *   <li>Los miembros inactivos no pueden realizar acciones en el proyecto</li>
 * </ul>
 * 
 * @see <a href="../../../../../docs/domain/project.md">Documentación completa del dominio</a>
 * @author diegoarnanz-maker
 * @version 1.0
 * @since 2025
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