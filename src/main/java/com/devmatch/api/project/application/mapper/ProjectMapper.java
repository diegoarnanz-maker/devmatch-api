package com.devmatch.api.project.application.mapper;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.domain.model.Project;
import com.devmatch.api.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.devmatch.api.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de proyectos
 */
@Component
@RequiredArgsConstructor
public class ProjectMapper {
    
    private final UserQueryUseCase userQueryUseCase;
    private final ProjectPersistenceMapper projectPersistenceMapper;
    
    /**
     * Convierte un DTO de solicitud a una entidad de dominio
     * @param requestDto DTO de solicitud
     * @param ownerId ID del propietario del proyecto
     * @return Entidad de dominio
     */
    public Project toDomain(ProjectRequestDto requestDto, Long ownerId) {
        return new Project(
            requestDto.getTitle(),
            requestDto.getDescription(),
            requestDto.getStatus(),
            ownerId,
            requestDto.getRepoUrl(),
            requestDto.getCoverImageUrl(),
            requestDto.getEstimatedDurationWeeks(),
            requestDto.getMaxTeamSize(),
            requestDto.isPublic()
        );
    }
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta
     * @param project Entidad de dominio
     * @return DTO de respuesta
     */
    public ProjectResponseDto toResponseDto(Project project) {
        String ownerUsername = null;
        List<ProjectResponseDto.ProjectMemberDto> teamMembers = new ArrayList<>();
        
        try {
            // Obtener información del owner
            var owner = userQueryUseCase.findUserById(project.getOwnerId());
            ownerUsername = owner.getUsername();
            
            // Obtener el primer profile type del usuario (por ahora solo el primero)
            String profileType = null;
            if (owner.getProfileTypes() != null && !owner.getProfileTypes().isEmpty()) {
                profileType = owner.getProfileTypes().get(0); // Tomar el primero
            }
            
            // Agregar el owner como miembro del equipo (líder)
            teamMembers.add(new ProjectResponseDto.ProjectMemberDto(
                owner.getId(),
                owner.getUsername(),
                "LEADER", // Posición en el proyecto
                profileType // Profile type del usuario
            ));
            
            // TODO: Agregar otros miembros del equipo cuando se implemente la funcionalidad
            // Por ahora solo está el owner
            
        } catch (Exception e) {
            // Si no se puede obtener la información del owner, continuar sin ella
            // Esto puede pasar si el usuario fue eliminado
            System.err.println("Error obteniendo información del owner para proyecto " + project.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return new ProjectResponseDto(
            project.getId(),
            project.getTitle(),
            project.getDescription(),
            project.getStatus(),
            project.getOwnerId(),
            project.getRepoUrl(),
            project.getCoverImageUrl(),
            project.getEstimatedDurationWeeks(),
            project.getMaxTeamSize(),
            ownerUsername,
            teamMembers,
            new ArrayList<>(), // tags - se manejarán en método sobrecargado
            project.isPublic(),
            project.isActive(),
            project.isDeleted(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
    
    /**
     * Convierte una entidad JPA a un DTO de respuesta (incluye tags)
     * @param projectEntity Entidad JPA del proyecto
     * @return DTO de respuesta con tags
     */
    public ProjectResponseDto toResponseDto(ProjectEntity projectEntity) {
        String ownerUsername = null;
        List<ProjectResponseDto.ProjectMemberDto> teamMembers = new ArrayList<>();
        
        try {
            // Obtener información del owner
            var owner = userQueryUseCase.findUserById(projectEntity.getOwnerId());
            ownerUsername = owner.getUsername();
            
            // Obtener el primer profile type del usuario (por ahora solo el primero)
            String profileType = null;
            if (owner.getProfileTypes() != null && !owner.getProfileTypes().isEmpty()) {
                profileType = owner.getProfileTypes().get(0); // Tomar el primero
            }
            
            // Agregar el owner como miembro del equipo (líder)
            teamMembers.add(new ProjectResponseDto.ProjectMemberDto(
                owner.getId(),
                owner.getUsername(),
                "LEADER", // Posición en el proyecto
                profileType // Profile type del usuario
            ));
            
            // TODO: Agregar otros miembros del equipo cuando se implemente la funcionalidad
            // Por ahora solo está el owner
            
        } catch (Exception e) {
            // Si no se puede obtener la información del owner, continuar sin ella
            // Esto puede pasar si el usuario fue eliminado
            System.err.println("Error obteniendo información del owner para proyecto " + projectEntity.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Extraer tags
        List<String> tags = projectPersistenceMapper.extractTagNames(projectEntity);
        
        return new ProjectResponseDto(
            projectEntity.getId(),
            projectEntity.getTitle(),
            projectEntity.getDescription(),
            projectEntity.getStatus(),
            projectEntity.getOwnerId(),
            projectEntity.getRepoUrl(),
            projectEntity.getCoverImageUrl(),
            projectEntity.getEstimatedDurationWeeks(),
            projectEntity.getMaxTeamSize(),
            ownerUsername,
            teamMembers,
            tags,
            projectEntity.isPublic(),
            projectEntity.isActive(),
            projectEntity.isDeleted(),
            projectEntity.getCreatedAt(),
            projectEntity.getUpdatedAt()
        );
    }

    /**
     * Convierte una lista de entidades de dominio a una lista de DTOs de respuesta
     * @param projects Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public List<ProjectResponseDto> toResponseDtoList(List<Project> projects) {
        return projects.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de entidades JPA a una lista de DTOs de respuesta (incluye tags)
     * @param projectEntities Lista de entidades JPA
     * @return Lista de DTOs de respuesta con tags
     */
    public List<ProjectResponseDto> toResponseDtoListWithTags(List<ProjectEntity> projectEntities) {
        return projectEntities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza una entidad existente con datos del DTO de solicitud
     * @param existingProject Proyecto existente
     * @param requestDto DTO con los nuevos datos
     * @return Nueva entidad actualizada
     */
    public Project updateProjectFromDto(Project existingProject, ProjectRequestDto requestDto) {
        return new Project(
            existingProject.getId(),
            requestDto.getTitle(),
            requestDto.getDescription(),
            requestDto.getStatus(),
            existingProject.getOwnerId(),
            requestDto.getRepoUrl(),
            requestDto.getCoverImageUrl(),
            requestDto.getEstimatedDurationWeeks(),
            requestDto.getMaxTeamSize(),
            requestDto.isPublic(),
            existingProject.isActive(),
            existingProject.isDeleted(),
            existingProject.getCreatedAt(),
            existingProject.getUpdatedAt()
        );
    }
} 