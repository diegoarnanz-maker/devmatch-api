package com.devmatch.api.projectreview.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un proyecto recibe una nueva review.
 */
public class ProjectReviewReceivedEvent extends BaseDomainEvent {
    
    private final Long projectId;
    private final String projectName;
    private final Long ownerId;
    private final Long reviewerId;
    private final String reviewerName;
    private final Long reviewId;
    
    public ProjectReviewReceivedEvent(Long projectId, String projectName, Long ownerId, Long reviewerId, String reviewerName, Long reviewId) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.reviewId = reviewId;
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
    
    public Long getReviewerId() {
        return reviewerId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public Long getReviewId() {
        return reviewId;
    }
}
