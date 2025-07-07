package com.devmatch.api.project.application.dto;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de proyectos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    
    private Long id;
    private String title;
    private String description;
    private ProjectStatus status;
    private Long ownerId;
    private String repoUrl;
    private String coverImageUrl;
    private Integer estimatedDurationWeeks;
    private Integer maxTeamSize;

    
    // Información del propietario
    private String ownerUsername;
    
    // Información de miembros del equipo
    private List<ProjectMemberDto> teamMembers;
    
    // Tecnologías asociadas al proyecto
    private List<String> tags;
    
    private boolean isPublic;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * DTO interno para miembros del proyecto
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMemberDto {
        private Long userId;
        private String username;
        private String position; // "LEADER", "DEVELOPER", "DESIGNER", etc.
        private String profileType; // "BACKEND", "FRONTEND", "FULLSTACK", etc.
    }
} 