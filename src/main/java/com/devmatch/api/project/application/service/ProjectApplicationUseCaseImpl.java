package com.devmatch.api.project.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import com.devmatch.api.project.application.mapper.ProjectApplicationMapper;
import com.devmatch.api.project.application.port.in.ProjectApplicationUseCase;
import com.devmatch.api.project.application.port.out.ProjectRepositoryPort;
import com.devmatch.api.project.application.port.out.ProjectMemberRepositoryPort;
import com.devmatch.api.project.application.port.out.ProjectApplicationRepositoryPort;
import com.devmatch.api.project.domain.exception.ProjectNotFoundException;
import com.devmatch.api.project.domain.exception.ProjectOperationNotAllowedException;
import com.devmatch.api.project.domain.model.Project;
import com.devmatch.api.project.domain.model.ProjectApplication;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectApplicationUseCaseImpl implements ProjectApplicationUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    private final ProjectApplicationRepositoryPort projectApplicationRepositoryPort;
    private final ProjectApplicationMapper projectApplicationMapper;

    @Override
    public void applyToProject(Long projectId, Long userId, String motivationMessage) {
        // 1. Validar que el proyecto existe
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // 2. Validar que el usuario existe
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));

        // 3. Validar que el proyecto está abierto para aplicaciones
        if (!project.isOpenForApplications()) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " no está abierto para aplicaciones");
        }

        // 4. Validar que el proyecto no está lleno
        int currentTeamSize = projectMemberRepositoryPort.countActiveMembersByProjectId(projectId);
        if (project.isFull(currentTeamSize)) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " ya está lleno");
        }

        // 5. Validar que el usuario no es el propietario
        if (project.isOwner(userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " no puede aplicar al proyecto");
        }

        // 6. Validar que el usuario no ha aplicado previamente
        if (projectApplicationRepositoryPort.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " ya ha aplicado al proyecto con ID " + projectId);
        }

        // 7. Crear la aplicación
        ProjectApplication application = new ProjectApplication(projectId, userId, motivationMessage);
        
        // 8. Guardar la aplicación
        projectApplicationRepositoryPort.save(application);
    }

    @Override
    public List<ProjectApplicationResponseDto> getProjectApplications(Long projectId, Long ownerId) {
        // 1. Validar que el proyecto existe
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // 2. Validar que el usuario es el propietario del proyecto
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        // 3. Obtener todas las aplicaciones del proyecto
        List<ProjectApplication> applications = projectApplicationRepositoryPort.findByProjectId(projectId);
        
        // 4. Marcar las aplicaciones como vistas por el owner y guardarlas
        List<ProjectApplication> updatedApplications = applications.stream()
                .map(application -> {
                    if (application.isSeenByOwner()) {
                        // Si ya está marcada como vista, no la modificamos
                        return application;
                    }
                    // Marcar como vista y guardar
                    ProjectApplication markedApplication = application.markAsSeen();
                    return projectApplicationRepositoryPort.save(markedApplication);
                })
                .toList();
        
        // 5. Convertir a DTOs y retornar
        return projectApplicationMapper.toResponseDtoList(updatedApplications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectApplicationResponseDto> getUserApplications(Long userId) {
        // 1. Validar que el usuario existe
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
        
        // 2. Obtener todas las aplicaciones del usuario
        List<ProjectApplication> applications = projectApplicationRepositoryPort.findByUserId(userId);
        
        // 3. Convertir a DTOs y retornar
        return projectApplicationMapper.toResponseDtoList(applications);
    }

    @Override
    public void acceptApplication(Long projectId, Long applicationId, Long ownerId) {
        // 1. Validar que el proyecto existe
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // 2. Validar que el usuario es el propietario del proyecto
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        // 3. Validar que la aplicación existe y pertenece al proyecto
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        if (!application.getProjectId().equals(projectId)) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no pertenece al proyecto con ID " + projectId);
        }
        
        // 4. Validar que la aplicación está pendiente
        if (!application.isPending()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " ya no está pendiente");
        }
        
        // 5. Validar que el proyecto no está lleno
        int currentTeamSize = projectMemberRepositoryPort.countActiveMembersByProjectId(projectId);
        if (project.isFull(currentTeamSize)) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " ya está lleno");
        }
        
        // 6. Aceptar la aplicación
        ProjectApplication acceptedApplication = application.accept();
        
        // 7. Guardar la aplicación actualizada
        projectApplicationRepositoryPort.save(acceptedApplication);
        
        // 8. Agregar al usuario como miembro del proyecto
        projectMemberRepositoryPort.addMember(projectId, acceptedApplication.getUserId(), "DEVELOPER");
    }

    @Override
    public void rejectApplication(Long projectId, Long applicationId, Long ownerId) {
        // 1. Validar que el proyecto existe
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        // 2. Validar que el usuario es el propietario del proyecto
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        // 3. Validar que la aplicación existe y pertenece al proyecto
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        if (!application.getProjectId().equals(projectId)) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no pertenece al proyecto con ID " + projectId);
        }
        
        // 4. Validar que la aplicación está pendiente
        if (!application.isPending()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " ya no está pendiente");
        }
        
        // 5. Rechazar la aplicación
        ProjectApplication rejectedApplication = application.reject();
        
        // 6. Guardar la aplicación actualizada
        projectApplicationRepositoryPort.save(rejectedApplication);
    }

    @Override
    public void cancelApplication(Long applicationId, Long userId) {
        // 1. Validar que la aplicación existe
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        // 2. Validar que el usuario es el que aplicó
        if (!application.getUserId().equals(userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " no puede cancelar la aplicación con ID " + applicationId);
        }
        
        // 3. Validar que la aplicación puede ser cancelada
        if (!application.canBeCancelled()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no puede ser cancelada");
        }
        
        // 4. Cancelar la aplicación
        ProjectApplication cancelledApplication = application.cancel();
        
        // 5. Guardar la aplicación actualizada
        projectApplicationRepositoryPort.save(cancelledApplication);
    }

}
