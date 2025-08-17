package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando una solicitud para unirse a un proyecto expira automáticamente
 * después de 7 días sin respuesta del propietario del proyecto.
 * Contiene la información básica de la solicitud expirada.
 */
public class ProjectApplicationExpiredEvent extends BaseDomainEvent {
    private final Long applicantId;   // ID del usuario que aplicó
    private final Long projectId;     // ID del proyecto
    private final String projectName; // Nombre del proyecto
    private final Long ownerId;       // ID del propietario del proyecto

    public ProjectApplicationExpiredEvent(Long applicantId, Long projectId, String projectName, Long ownerId) {
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
