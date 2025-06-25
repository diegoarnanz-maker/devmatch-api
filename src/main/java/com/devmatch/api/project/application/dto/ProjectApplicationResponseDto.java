package com.devmatch.api.project.application.dto;

import com.devmatch.api.project.domain.model.valueobject.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una aplicación a un proyecto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectApplicationResponseDto {

    private Long id;
    private Long projectId;
    private Long userId;
    private String userUsername;
    private String userFirstName;
    private String userLastName;
    private List<String> userProfileTypes; // Tipos de perfil del usuario (BACKEND, FRONTEND, etc.)
    private String motivationMessage;
    private ApplicationStatus status;
    private boolean seenByOwner;
    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 