package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un nuevo miembro se une al proyecto.
 * Contiene la información básica del nuevo miembro y del proyecto.
 */
public class ProjectMemberJoinedEvent extends BaseDomainEvent {
    private final Long newMemberId;   // ID del nuevo miembro
    private final Long projectId;     // ID del proyecto
    private final String projectName; // Nombre del proyecto
    private final String newMemberRole; // Rol del nuevo miembro

    public ProjectMemberJoinedEvent(Long newMemberId, Long projectId, String projectName, String newMemberRole) {
        this.newMemberId = newMemberId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.newMemberRole = newMemberRole;
    }

    public Long getNewMemberId() {
        return newMemberId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getNewMemberRole() {
        return newMemberRole;
    }
}
