package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario envía una solicitud para unirse a un proyecto.
 * Contiene la información básica de la solicitud.
 */
public class ProjectApplicationSubmittedEvent extends BaseDomainEvent {
    private final Long ownerId;      // ID del propietario del proyecto
    private final Long projectId;    // ID del proyecto
    private final String projectName; // Nombre del proyecto
    private final Long applicantId;  // ID del usuario que solicita

    public ProjectApplicationSubmittedEvent(Long ownerId, Long projectId, String projectName, Long applicantId) {
        this.ownerId = ownerId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.applicantId = applicantId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getApplicantId() {
        return applicantId;
    }
}
