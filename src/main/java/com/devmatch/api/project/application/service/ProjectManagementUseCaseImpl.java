package com.devmatch.api.project.application.service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        long userProjectCount = projectRepositoryPort.countByOwnerId(ownerId);
        projectDomainService.validateProjectCreation(ownerId, userProjectCount);

        Project project = projectMapper.toDomain(request, ownerId);

        Project savedProject = projectRepositoryPort.save(project);

        // Procesar tags si se proporcionaron
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            System.out.println("Procesando tags: " + request.getTags());
            try {
                List<Long> tagIds = new ArrayList<>();
                for (String tagName : request.getTags()) {
                    System.out.println("Procesando tag: " + tagName);
                    // Buscar o crear el tag
                    TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                            .orElseGet(() -> {
                                System.out.println("Creando nuevo tag: " + tagName);
                                return tagRepositoryPort.createTag(tagName, "TECHNOLOGY");
                            });
                    tagIds.add(tag.id());
                    System.out.println("Tag procesado: " + tagName + " con ID: " + tag.id());
                }
                
                System.out.println("Agregando tags al proyecto: " + tagIds);
                // Agregar tags al proyecto
                projectRepositoryPort.addTagsToProject(savedProject.getId(), tagIds);
                System.out.println("Tags agregados exitosamente");
            } catch (Exception e) {
                // Si hay error con tags, continuar sin tags
                System.err.println("Error procesando tags: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Retornar proyecto con tags incluidos
        try {
            ProjectEntity projectWithTags = projectJpaRepository.findByIdWithTags(savedProject.getId())
                    .orElseThrow(() -> new ProjectNotFoundException(savedProject.getId()));
            return projectMapper.toResponseDto(projectWithTags);
        } catch (Exception e) {
            // Si hay error cargando tags, retornar sin tags
            System.err.println("Error cargando proyecto con tags: " + e.getMessage());
            e.printStackTrace();
            return projectMapper.toResponseDto(savedProject);
        }
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request, Long userId) {
        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "editar");
        }

        Project updatedProject = projectMapper.updateProjectFromDto(existingProject, request);

        Project savedProject = projectRepositoryPort.save(updatedProject);

        // Procesar tags si se proporcionaron
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (String tagName : request.getTags()) {
                // Buscar o crear el tag
                TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                        .orElseGet(() -> tagRepositoryPort.createTag(tagName, "TECHNOLOGY"));
                tagIds.add(tag.id());
            }

            // Agregar tags al proyecto
            projectRepositoryPort.addTagsToProject(projectId, tagIds);
        }

        // Obtener el proyecto actualizado con tags
        try {
            ProjectEntity projectWithTags = projectJpaRepository.findByIdWithTags(savedProject.getId())
                    .orElseThrow(() -> new ProjectNotFoundException(savedProject.getId()));
            return projectMapper.toResponseDto(projectWithTags);
        } catch (Exception e) {
            // Si hay error cargando tags, retornar sin tags
            System.err.println("Error cargando proyecto con tags: " + e.getMessage());
            e.printStackTrace();
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
    public void deactivateProject(Long projectId, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "desactivar");
        }

        Project deactivatedProject = existingProject.deactivate();

        projectRepositoryPort.save(deactivatedProject);
    }

    @Override
    public void deleteProject(Long projectId, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "eliminar");
        }

        Project deletedProject = existingProject.softDelete();

        projectRepositoryPort.save(deletedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsByOwner(Long ownerId) {

        List<Project> projects = projectRepositoryPort.findByOwnerId(ownerId);

        return projectMapper.toResponseDtoList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId, Long userId) {

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
        // 1. Validar que el proyecto existe
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // 2. Validar que el usuario puede ver el proyecto
        if (!project.isVisibleTo(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "ver miembros");
        }
        
        // 3. Obtener los miembros del proyecto
        List<ProjectMember> members = projectMemberRepositoryPort.getActiveMembersByProjectId(projectId);
        
        // 4. Convertir a DTOs
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
    public List<ProjectResponseDto> getAllPublicProjects() {
        // Obtener entidades JPA con tags cargados
        List<ProjectEntity> projectEntities = projectJpaRepository.findPublicActiveProjectsWithTags();
        
        // Convertir a DTOs con tags incluidos
        return projectMapper.toResponseDtoListWithTags(projectEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> searchPublicProjects(ProjectPublicSearchRequestDto filter) {
        // Convertir el status de String a ProjectStatus si no es null
        ProjectStatus status = null;
        if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty()) {
            try {
                status = ProjectStatus.valueOf(filter.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si el status no es válido, retornar lista vacía o lanzar excepción
                throw new IllegalArgumentException("Estado de proyecto inválido: " + filter.getStatus());
            }
        }
        
        // Obtener entidades JPA con tags cargados
        List<ProjectEntity> projectEntities = projectJpaRepository.searchPublicProjectsWithTags(
                filter.getTitle(),
                status,
                filter.getIsActive(),
                filter.getMinTeamSize(),
                filter.getMaxTeamSize(),
                filter.getMinDurationWeeks(),
                filter.getMaxDurationWeeks()
        );
        
        // Convertir a DTOs con tags incluidos
        return projectMapper.toResponseDtoListWithTags(projectEntities);
    }

    @Override
    @Transactional
    public ProjectResponseDto addTagsToProject(Long projectId, ProjectTagsRequestDto request, Long userId) {
        // Verificar que el proyecto existe y el usuario puede editarlo
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "agregar tags");
        }

        // Procesar cada tag
        List<Long> tagIds = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            // Buscar o crear el tag
            TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                    .orElseGet(() -> tagRepositoryPort.createTag(tagName, "TECHNOLOGY"));
            tagIds.add(tag.id());
        }

        // Agregar tags al proyecto
        projectRepositoryPort.addTagsToProject(projectId, tagIds);

        // Retornar proyecto actualizado
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
        // Verificar que el proyecto existe y el usuario puede editarlo
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "remover tags");
        }

        // Buscar el tag por nombre
        TagRepositoryPort.TagDto tag = tagRepositoryPort.findByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado: " + tagName));

        // Remover el tag del proyecto
        projectRepositoryPort.removeTagFromProject(projectId, tag.id());

        // Retornar proyecto actualizado
        ProjectEntity updatedProject = projectJpaRepository.findPublicActiveProjectsWithTags()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return projectMapper.toResponseDto(updatedProject);
    }
}
