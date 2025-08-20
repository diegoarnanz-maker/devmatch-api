package com.devmatch.api.projectreview.domain.model;

import java.time.LocalDateTime;
import com.devmatch.api.projectreview.domain.model.valueobject.Rating;
import com.devmatch.api.projectreview.domain.model.valueobject.Comment;

public class Review {
    private Long id;
    private Long projectId;
    private Long userId;
    private Rating rating;
    private Comment comment;
    private boolean isPublic;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos para la respuesta del propietario
    private Comment ownerResponse;
    private boolean ownerResponsePublic;
    private LocalDateTime ownerResponseDate;

    public Review() {
    }

    public Review(Long id, Long projectId, Long userId, Rating rating, Comment comment, boolean isPublic, boolean isActive, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.isPublic = isPublic;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ownerResponse = null;
        this.ownerResponsePublic = true;
        this.ownerResponseDate = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Getters y setters para la respuesta del propietario
    public Comment getOwnerResponse() {
        return ownerResponse;
    }

    public void setOwnerResponse(Comment ownerResponse) {
        this.ownerResponse = ownerResponse;
    }

    public boolean isOwnerResponsePublic() {
        return ownerResponsePublic;
    }

    public void setOwnerResponsePublic(boolean ownerResponsePublic) {
        this.ownerResponsePublic = ownerResponsePublic;
    }

    public LocalDateTime getOwnerResponseDate() {
        return ownerResponseDate;
    }

    public void setOwnerResponseDate(LocalDateTime ownerResponseDate) {
        this.ownerResponseDate = ownerResponseDate;
    }
}
