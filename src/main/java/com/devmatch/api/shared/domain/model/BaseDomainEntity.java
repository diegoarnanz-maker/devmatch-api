package com.devmatch.api.shared.domain.model;

import java.time.LocalDateTime;

public class BaseDomainEntity {

    protected Long id;
    protected boolean isActive = true;
    protected boolean isDeleted = false;
    protected LocalDateTime createdAt = LocalDateTime.now();
    protected LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void markDeleted() {
        this.isDeleted = true;
        this.isActive = false;
    }

    public void deactivate() {
        if (!this.isDeleted) {
            this.isActive = false;
        }
    }    

    public void reactivate() {
        if (!this.isDeleted) {
            this.isActive = true;
        }
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}