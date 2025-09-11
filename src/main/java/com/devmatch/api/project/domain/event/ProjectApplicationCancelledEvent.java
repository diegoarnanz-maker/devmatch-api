package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento cuando se cancela aplicación a proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectApplicationCancelledEvent extends BaseDomainEvent {
    private final Long applicantId;
    private final Long projectId;
    private final String projectName;
    private final Long ownerId;

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
