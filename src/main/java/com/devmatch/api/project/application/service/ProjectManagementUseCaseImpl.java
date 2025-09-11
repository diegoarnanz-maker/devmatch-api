package com.devmatch.api.project.application.service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.application.dto.ProjectPublicSearchRequestDto;
import com.devmatch.api.project.application.dto.ProjectTagsRequestDto;
import com.devmatch.api.project.application.mapper.ProjectMapper;
import com.devmatch.api.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.devmatch.api.project.infrastructure.out.persistence.repository.ProjectJpaRepository;
import com.devmatch.api.project.application.port.out.TagRepositoryPort;
import com.devmatch.api.project.application.port.in.ProjectManagementUseCase;
import com.devmatch.api.project.application.port.out.ProjectRepositoryPort;
import com.devmatch.api.project.application.port.out.ProjectMemberRepositoryPort;
import com.devmatch.api.project.domain.exception.ProjectNotFoundException;
import com.devmatch.api.project.domain.exception.ProjectOperationNotAllowedException;
import com.devmatch.api.project.domain.model.Project;
import com.devmatch.api.project.domain.model.ProjectMember;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.project.domain.service.ProjectDomainService;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del caso de uso para gestión de proyectos.
 * 
 * <p>Este servicio proporciona operaciones completas de gestión de proyectos,
 * incluyendo creación, actualización, eliminación y consulta de proyectos,
 * así como gestión de miembros y tags asociados.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Crear y configurar nuevos proyectos con validaciones de negocio.</li>
 *   <li>Actualizar información de proyectos existentes.</li>
 *   <li>Gestionar estados y visibilidad de proyectos.</li>
 *   <li>Administrar miembros del equipo y sus roles.</li>
 *   <li>Gestionar tags y tecnologías asociadas a proyectos.</li>
 *   <li>Proporcionar consultas paginadas y filtradas de proyectos.</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Las solicitudes llegan desde la capa de presentación (controladores).</li>
 *   <li>Se aplican validaciones de dominio a través de {@code ProjectDomainService}.</li>
 *   <li>Se interactúa con repositorios para persistir y recuperar datos.</li>
 *   <li>Se utilizan mappers para convertir entre entidades de dominio y DTOs.</li>
 *   <li>Se gestionan relaciones con tags y miembros del proyecto.</li>
 * </ol>
 * 
 * <h3>Consideraciones de negocio:</h3>
 * <ul>
 *   <li>Todas las operaciones están transaccionalmente gestionadas.</li>
 *   <li>Se aplican validaciones de permisos y reglas de negocio.</li>
 *   <li>Los proyectos se crean automáticamente con el propietario como miembro.</li>
 *   <li>Los tags se crean dinámicamente si no existen.</li>
 *   <li>Se manejan excepciones específicas del dominio.</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectManagementUseCaseImpl implements ProjectManagementUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectDomainService projectDomainService;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    private final UserQueryUseCase userQueryUseCase;
    private final ProjectJpaRepository projectJpaRepository;
    private final TagRepositoryPort tagRepositoryPort;

    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto request, Long ownerId) {
        // Validar límites y crear proyecto
        long userProjectCount = projectRepositoryPort.countByOwnerId(ownerId);
        projectDomainService.validateProjectCreation(ownerId, userProjectCount);

        Project project = projectMapper.toDomain(request, ownerId);

        Project savedProject = projectRepositoryPort.save(project);

        // Agregar propietario como miembro y procesar tags
        projectMemberRepositoryPort.addMember(savedProject.getId(), ownerId, "OWNER", true);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                List<Long> tagIds = new ArrayList<>();
                for (String tagName : request.getTags()) {
                    // Buscar o crear el tag
                    TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                            .orElseGet(() -> tagRepositoryPort.createTag(tagName, "TECHNOLOGY"));
                    tagIds.add(tag.id());
                }
                
                projectRepositoryPort.addTagsToProject(savedProject.getId(), tagIds);
            } catch (Exception e) {
                log.warn("Error procesando tags para proyecto {}: {}", savedProject.getId(), e.getMessage());
                // Continuar sin tags para no interrumpir la creación del proyecto
            }
        }

        // Retornar proyecto con tags
        try {
            ProjectEntity projectWithTags = projectJpaRepository.findByIdWithTags(savedProject.getId())
                    .orElseThrow(() -> new ProjectNotFoundException(savedProject.getId()));
            return projectMapper.toResponseDto(projectWithTags);
        } catch (Exception e) {
            log.warn("Error cargando proyecto con tags para ID {}: {}", savedProject.getId(), e.getMessage());
            // Retornar proyecto sin tags si hay error cargando las relaciones
            return projectMapper.toResponseDto(savedProject);
        }
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request, Long userId) {
        // Validar permisos y actualizar proyecto
        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "editar");
        }

        Project updatedProject = projectMapper.updateProjectFromDto(existingProject, request);

        Project savedProject = projectRepositoryPort.save(updatedProject);

        // Procesar tags
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (String tagName : request.getTags()) {
                // Buscar o crear el tag
                TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                        .orElseGet(() -> tagRepositoryPort.createTag(tagName, "TECHNOLOGY"));
                tagIds.add(tag.id());
            }

            projectRepositoryPort.addTagsToProject(projectId, tagIds);
        }

        // Retornar proyecto con tags
        try {
            ProjectEntity projectWithTags = projectJpaRepository.findByIdWithTags(savedProject.getId())
                    .orElseThrow(() -> new ProjectNotFoundException(savedProject.getId()));
            return projectMapper.toResponseDto(projectWithTags);
        } catch (Exception e) {
            log.warn("Error cargando proyecto con tags para ID {}: {}", savedProject.getId(), e.getMessage());
            // Retornar proyecto sin tags si hay error cargando las relaciones
            return projectMapper.toResponseDto(savedProject);
        }
    }

    @Override
    public ProjectResponseDto changeProjectStatus(Long projectId, ProjectStatus newStatus, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "cambiar estado");
        }

        // Cargar la entidad JPA con tags para preservar las relaciones
        ProjectEntity projectEntity = projectJpaRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Actualizar solo el estado en la entidad JPA (preserva tags)
        projectEntity.setStatus(newStatus);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        // Guardar la entidad actualizada (preserva las relaciones con tags)
        ProjectEntity savedEntity = projectJpaRepository.save(projectEntity);

        // Retornar el DTO con tags
        return projectMapper.toResponseDto(savedEntity);
    }

    @Override
    public ProjectResponseDto changeProjectVisibility(Long projectId, boolean isPublic, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "cambiar visibilidad");
        }

        // Cargar la entidad JPA con tags para preservar las relaciones
        ProjectEntity projectEntity = projectJpaRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Actualizar solo la visibilidad en la entidad JPA (preserva tags)
        projectEntity.setPublic(isPublic);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        // Guardar la entidad actualizada (preserva las relaciones con tags)
        ProjectEntity savedEntity = projectJpaRepository.save(projectEntity);

        // Retornar el DTO con tags
        return projectMapper.toResponseDto(savedEntity);
    }

    @Override
    public ProjectResponseDto deactivateProject(Long projectId, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "desactivar");
        }

        // Cargar la entidad JPA con tags para preservar las relaciones
        ProjectEntity projectEntity = projectJpaRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Actualizar solo el estado activo en la entidad JPA (preserva tags)
        projectEntity.setActive(false);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        // Guardar la entidad actualizada (preserva las relaciones con tags)
        ProjectEntity savedEntity = projectJpaRepository.save(projectEntity);

        // Retornar el DTO con tags
        return projectMapper.toResponseDto(savedEntity);
    }

    @Override
    public ProjectResponseDto deleteProject(Long projectId, Long userId) {
        // Validar permisos y eliminar proyecto
        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "eliminar");
        }

        // Cargar la entidad JPA con tags para preservar las relaciones
        ProjectEntity projectEntity = projectJpaRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Actualizar solo los campos de estado en la entidad JPA (preserva tags)
        projectEntity.setActive(false);
        projectEntity.setDeleted(true);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        // Guardar la entidad actualizada (preserva las relaciones con tags)
        ProjectEntity savedEntity = projectJpaRepository.save(projectEntity);

        // Retornar el DTO con tags
        return projectMapper.toResponseDto(savedEntity);
    }

    @Override
    public ProjectResponseDto restoreProject(Long projectId, Long userId) {
        // Buscar y restaurar proyecto
        ProjectEntity projectEntity = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Verificar que el usuario es el propietario
        if (!projectEntity.getOwnerId().equals(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "restaurar");
        }

        // Cargar la entidad JPA con tags para preservar las relaciones
        ProjectEntity projectWithTags = projectJpaRepository.findByIdWithTags(projectId)
                .orElse(projectEntity); // Si no encuentra con tags, usar la entidad básica

        // Actualizar solo los campos de estado en la entidad JPA (preserva tags)
        projectWithTags.setActive(true);
        projectWithTags.setDeleted(false);
        projectWithTags.setUpdatedAt(LocalDateTime.now());

        // Guardar la entidad actualizada (preserva las relaciones con tags)
        ProjectEntity savedEntity = projectJpaRepository.save(projectWithTags);

        // Retornar el DTO con tags
        return projectMapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByOwner(Long ownerId, Pageable pageable) {
        // Obtener proyectos del propietario
        return projectRepositoryPort.findByOwnerId(ownerId, pageable)
            .map(projectMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsByOwnerWithSecurity(Long ownerId, Long authenticatedUserId, ProjectPublicSearchRequestDto filter) {
        // Obtener proyectos con validación de seguridad
        if (ownerId.equals(authenticatedUserId)) {
            Page<ProjectEntity> allProjectsPage = projectJpaRepository.findByOwnerIdAndIsDeletedFalse(ownerId, Pageable.unpaged());
            List<ProjectEntity> allProjectEntities = allProjectsPage.getContent();
            
            // Aplicar filtros si se proporcionan
            if (filter != null) {
                allProjectEntities = allProjectEntities.stream()
                        .filter(projectEntity -> applyFilters(projectEntity, filter))
                        .toList();
            }
            
            return allProjectEntities.stream()
                    .map(projectMapper::toResponseDto)
                    .toList();
        }
        
        // Si no es el propietario, solo devolver proyectos públicos
        Page<ProjectEntity> publicProjectsPage = projectJpaRepository.findByIsPublicTrueAndIsActiveTrueAndIsDeletedFalse(Pageable.unpaged());
        List<ProjectEntity> publicProjectEntities = publicProjectsPage.getContent().stream()
                .filter(projectEntity -> projectEntity.getOwnerId().equals(ownerId))
                .toList();
        
        // Aplicar filtros si se proporcionan
        if (filter != null) {
            publicProjectEntities = publicProjectEntities.stream()
                    .filter(projectEntity -> applyFilters(projectEntity, filter))
                    .toList();
        }
        
        return publicProjectEntities.stream()
                .map(projectMapper::toResponseDto)
                .toList();
    }
    
    /**
     * Método auxiliar para aplicar filtros a un proyecto
     */
    private boolean applyFilters(ProjectEntity projectEntity, ProjectPublicSearchRequestDto filter) {
        // Filtrar por título (búsqueda parcial)
        if (filter.getTitle() != null && !filter.getTitle().trim().isEmpty()) {
            if (!projectEntity.getTitle().toLowerCase().contains(filter.getTitle().toLowerCase())) {
                return false;
            }
        }
        
        // Filtrar por estado
        if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty()) {
            try {
                ProjectStatus filterStatus = ProjectStatus.valueOf(filter.getStatus().toUpperCase());
                if (!projectEntity.getStatus().equals(filterStatus)) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                // Si el status no es válido, no filtrar por él
            }
        }
        
        // Filtrar por estado activo
        if (filter.getIsActive() != null) {
            if (projectEntity.isActive() != filter.getIsActive()) {
                return false;
            }
        }
        
        // Filtrar por tamaño del equipo
        if (filter.getMinTeamSize() != null) {
            if (projectEntity.getMaxTeamSize() < filter.getMinTeamSize()) {
                return false;
            }
        }
        
        if (filter.getMaxTeamSize() != null) {
            if (projectEntity.getMaxTeamSize() > filter.getMaxTeamSize()) {
                return false;
            }
        }
        
        // Filtrar por duración estimada
        if (filter.getMinDurationWeeks() != null) {
            if (projectEntity.getEstimatedDurationWeeks() < filter.getMinDurationWeeks()) {
                return false;
            }
        }
        
        if (filter.getMaxDurationWeeks() != null) {
            if (projectEntity.getEstimatedDurationWeeks() > filter.getMaxDurationWeeks()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId, Long userId) {
        // Validar permisos y obtener proyecto
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.isVisibleTo(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "ver");
        }

        return projectMapper.toResponseDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getPublicProjectById(Long projectId) {
        // Obtener proyecto público
        ProjectEntity projectEntity = projectJpaRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // Verificar que el proyecto sea público y activo
        if (!projectEntity.isPublic() || !projectEntity.isActive()) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " no está disponible públicamente");
        }

        return projectMapper.toResponseDto(projectEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto.ProjectMemberDto> getProjectMembers(Long projectId, Long userId) {
        // Validar permisos y obtener miembros
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.isVisibleTo(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "ver miembros");
        }
        
        // Obtener miembros y convertir a DTOs
        List<ProjectMember> members = projectMemberRepositoryPort.getActiveMembersByProjectId(projectId);
        
        return members.stream()
                .map(member -> {
                    try {
                        var user = userQueryUseCase.findUserById(member.getUserId());
                        String profileType = null;
                        if (user.getProfileTypes() != null && !user.getProfileTypes().isEmpty()) {
                            profileType = user.getProfileTypes().get(0);
                        }
                        
                        return new ProjectResponseDto.ProjectMemberDto(
                                member.getUserId(),
                                user.getUsername(),
                                member.getMemberRole(),
                                profileType
                        );
                    } catch (Exception e) {
                        // Si no se puede obtener la información del usuario, crear DTO con datos básicos
                        return new ProjectResponseDto.ProjectMemberDto(
                                member.getUserId(),
                                "Usuario " + member.getUserId(),
                                member.getMemberRole(),
                                null
                        );
                    }
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getAllPublicProjects(Pageable pageable) {
        // Obtener todos los proyectos públicos
        return projectRepositoryPort.findPublicActiveProjects(pageable)
            .map(projectMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> searchPublicProjects(ProjectPublicSearchRequestDto filter, Pageable pageable) {
        // Buscar proyectos públicos con filtros
        return projectRepositoryPort.searchPublicProjects(filter, pageable)
            .map(projectMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProjectResponseDto addTagsToProject(Long projectId, ProjectTagsRequestDto request, Long userId) {
        // Validar permisos y agregar tags
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "agregar tags");
        }

        // Crear/buscar tags y agregar al proyecto
        List<Long> tagIds = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            // Buscar o crear el tag
            TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                    .orElseGet(() -> tagRepositoryPort.createTag(tagName, "TECHNOLOGY"));
            tagIds.add(tag.id());
        }

        projectRepositoryPort.addTagsToProject(projectId, tagIds);

        ProjectEntity updatedProject = projectJpaRepository.findPublicActiveProjectsWithTags()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return projectMapper.toResponseDto(updatedProject);
    }

    @Override
    @Transactional
    public ProjectResponseDto removeTagFromProject(Long projectId, String tagName, Long userId) {
        // Validar permisos y remover tag
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "remover tags");
        }

        // Buscar tag y remover del proyecto
        TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado: " + tagName));

        projectRepositoryPort.removeTagFromProject(projectId, tag.id());

        ProjectEntity updatedProject = projectJpaRepository.findPublicActiveProjectsWithTags()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return projectMapper.toResponseDto(updatedProject);
    }

    @Override
    @Transactional
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {
        // Validar permisos y remover miembro
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "remover miembros");
        }

        projectMemberRepositoryPort.removeMemberFromProject(projectId, memberId);
    }

    @Override
    @Transactional
    public ProjectResponseDto.ProjectMemberDto changeMemberRole(Long projectId, Long memberId, String newRole, Long userId) {
        // Validar permisos y cambiar rol
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "cambiar roles");
        }

        // Actualizar rol y obtener miembro actualizado
        projectMemberRepositoryPort.updateMemberRole(projectId, memberId, newRole);

        List<ProjectMember> members = projectMemberRepositoryPort.getActiveMembersByProjectId(projectId);
        ProjectMember updatedMember = members.stream()
                .filter(member -> member.getUserId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new ProjectOperationNotAllowedException(projectId, memberId, "miembro no encontrado"));

        try {
            var user = userQueryUseCase.findUserById(updatedMember.getUserId());
            String profileType = null;
            if (user.getProfileTypes() != null && !user.getProfileTypes().isEmpty()) {
                profileType = user.getProfileTypes().get(0);
            }
            
            return new ProjectResponseDto.ProjectMemberDto(
                    updatedMember.getUserId(),
                    user.getUsername(),
                    updatedMember.getMemberRole(),
                    profileType
            );
        } catch (Exception e) {
            // Si no se puede obtener la información del usuario, crear DTO con datos básicos
            return new ProjectResponseDto.ProjectMemberDto(
                    updatedMember.getUserId(),
                    "Usuario " + updatedMember.getUserId(),
                    updatedMember.getMemberRole(),
                    null
            );
        }
    }
}
