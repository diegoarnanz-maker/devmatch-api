package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario cancela su solicitud para unirse a un proyecto.
 * Contiene la información básica de la solicitud cancelada.
 */
public class ProjectApplicationCancelledEvent extends BaseDomainEvent {
    private final Long applicantId;   // ID del usuario que canceló la solicitud
    private final Long projectId;     // ID del proyecto
    private final String projectName; // Nombre del proyecto
    private final Long ownerId;       // ID del propietario del proyecto

    public ProjectApplicationCancelledEvent(Long applicantId, Long projectId, String projectName, Long ownerId) {
        this.applicantId = applicantId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.ownerId = ownerId;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getOwnerId() {
        return ownerId;
    }
}
