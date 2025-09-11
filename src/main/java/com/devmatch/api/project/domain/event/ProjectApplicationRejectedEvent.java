package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento cuando se rechaza aplicación a proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectApplicationRejectedEvent extends BaseDomainEvent {
    private final Long applicantId;
    private final Long projectId;
    private final String projectName;

    public ProjectApplicationRejectedEvent(Long applicantId, Long projectId, String projectName) {
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
