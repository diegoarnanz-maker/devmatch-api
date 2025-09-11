package com.devmatch.api.project.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento cuando nuevo miembro se une al proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public class ProjectMemberJoinedEvent extends BaseDomainEvent {
    private final Long newMemberId;
    private final Long projectId;
    private final String projectName;
    private final String newMemberRole;

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
