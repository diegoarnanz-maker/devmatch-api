package com.devmatch.api.project.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.application.mapper.ProjectMapper;
import com.devmatch.api.project.application.port.in.ProjectManagementUseCase;
import com.devmatch.api.project.application.port.out.ProjectRepositoryPort;
import com.devmatch.api.project.domain.exception.ProjectNotFoundException;
import com.devmatch.api.project.domain.exception.ProjectOperationNotAllowedException;
import com.devmatch.api.project.domain.model.Project;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.project.domain.service.ProjectDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectManagementUseCaseImpl implements ProjectManagementUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectMapper projectMapper;
    private final ProjectDomainService projectDomainService;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto request, Long ownerId) {

        long userProjectCount = projectRepositoryPort.countByOwnerId(ownerId);
        projectDomainService.validateProjectCreation(ownerId, userProjectCount);
        
        Project project = projectMapper.toDomain(request, ownerId);
        
        Project savedProject = projectRepositoryPort.save(project);
        
        return projectMapper.toResponseDto(savedProject);
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
        
        return projectMapper.toResponseDto(savedProject);
    }

    @Override
    public ProjectResponseDto changeProjectStatus(Long projectId, ProjectStatus newStatus, Long userId) {

        Project existingProject = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!existingProject.canBeEditedBy(userId)) {
            throw new ProjectOperationNotAllowedException(projectId, userId, "cambiar estado");
        }
        
        Project updatedProject = existingProject.updateStatus(newStatus);
        
        Project savedProject = projectRepositoryPort.save(updatedProject);
        
        return projectMapper.toResponseDto(savedProject);
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

        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // Verificar que el proyecto sea público y activo
        if (!project.isPublic() || !project.isActive()) {
            throw new ProjectOperationNotAllowedException(
                "El proyecto con ID " + projectId + " no está disponible públicamente"
            );
        }
        
        return projectMapper.toResponseDto(project);
    }
}
