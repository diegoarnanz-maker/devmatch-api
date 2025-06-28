package com.devmatch.api.user.application.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminProfileTypeResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 