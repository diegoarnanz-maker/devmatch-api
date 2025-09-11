package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento cuando se envía aplicación a proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectApplicationSubmittedEvent extends BaseDomainEvent {
    private final Long ownerId;
    private final Long projectId;
    private final String projectName;
    private final Long applicantId;

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
