package com.devmatch.api.project.application.mapper;

import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import com.devmatch.api.project.domain.model.ProjectApplication;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de aplicaciones de proyectos
 */
@Component
@RequiredArgsConstructor
public class ProjectApplicationMapper {
    
    private final UserQueryUseCase userQueryUseCase;
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta
     * @param application Entidad de dominio
     * @return DTO de respuesta
     */
    public ProjectApplicationResponseDto toResponseDto(ProjectApplication application) {
        try {
            // Obtener información del usuario que aplicó
            var user = userQueryUseCase.findUserById(application.getUserId());
            
            // Manejar profileTypes para evitar null
            List<String> profileTypes = user.getProfileTypes();
            if (profileTypes == null) {
                profileTypes = List.of();
            }
            
            return ProjectApplicationResponseDto.builder()
                .id(application.getId())
                .projectId(application.getProjectId())
                .userId(application.getUserId())
                .userUsername(user.getUsername())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .userProfileTypes(profileTypes)
                .motivationMessage(application.getMotivationMessage())
                .status(application.getStatus())
                .seenByOwner(application.isSeenByOwner())
                .submittedAt(application.getSubmittedAt())
                .resolvedAt(application.getResolvedAt())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
                
        } catch (Exception e) {
            // Si no se puede obtener la información del usuario, crear DTO con datos básicos
            System.err.println("Error obteniendo información del usuario para aplicación " + application.getId() + ": " + e.getMessage());
            
            return ProjectApplicationResponseDto.builder()
                .id(application.getId())
                .projectId(application.getProjectId())
                .userId(application.getUserId())
                .userUsername("Usuario no disponible")
                .userFirstName("N/A")
                .userLastName("N/A")
                .userProfileTypes(List.of())
                .motivationMessage(application.getMotivationMessage())
                .status(application.getStatus())
                .seenByOwner(application.isSeenByOwner())
                .submittedAt(application.getSubmittedAt())
                .resolvedAt(application.getResolvedAt())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
        }
    }
    
    /**
     * Convierte una lista de entidades de dominio a una lista de DTOs de respuesta
     * @param applications Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public List<ProjectApplicationResponseDto> toResponseDtoList(List<ProjectApplication> applications) {
        return applications.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
} 