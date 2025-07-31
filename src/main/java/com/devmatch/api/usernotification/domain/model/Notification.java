package com.devmatch.api.usernotification.domain.model;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationMessage;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;

import java.time.LocalDateTime;

public class Notification extends BaseDomainEntity {
    private Long userId;
    private NotificationMessage message;
    private NotificationType notificationType;
    private Long projectId;
    private Long reviewId;
    private String achievementCode;
    private boolean isRead;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Notification(Long id, Long userId, NotificationMessage message, NotificationType notificationType,
                        Long projectId, Long reviewId, String achievementCode, boolean isRead, boolean isActive,
                        boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.notificationType = notificationType;
        this.projectId = projectId;
        this.reviewId = reviewId;
        this.achievementCode = achievementCode;
        this.isRead = isRead;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NotificationMessage getMessage() {
        return message;
    }

    public void setMessage(NotificationMessage message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getAchievementCode() {
        return achievementCode;
    }

    public void setAchievementCode(String achievementCode) {
        this.achievementCode = achievementCode;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
} 