package com.devmatch.api.projectreview.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando se responde a una review de un proyecto.
 */
public class ProjectReviewResponseEvent extends BaseDomainEvent {
    
    private final Long projectId;
    private final String projectName;
    private final Long reviewerId;
    private final String reviewerName;
    private final Long ownerId;
    private final String ownerName;
    
    public ProjectReviewResponseEvent(Long projectId, String projectName, Long reviewerId, String reviewerName, Long ownerId, String ownerName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public Long getReviewerId() {
        return reviewerId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
}
