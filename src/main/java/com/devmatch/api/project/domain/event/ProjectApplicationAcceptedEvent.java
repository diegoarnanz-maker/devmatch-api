package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se acepta una solicitud para unirse a un proyecto.
 * Contiene la información básica de la solicitud aceptada.
 */
public class ProjectApplicationAcceptedEvent extends BaseDomainEvent {
    private final Long applicantId;   // ID del usuario que fue aceptado
    private final Long projectId;     // ID del proyecto
    private final String projectName; // Nombre del proyecto

    public ProjectApplicationAcceptedEvent(Long applicantId, Long projectId, String projectName) {
        this.applicantId = applicantId;
        this.projectId = projectId;
        this.projectName = projectName;
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
}
