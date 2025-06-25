package com.devmatch.api.project.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProjectApplications'");
    }

    @Override
    public void acceptApplication(Long projectId, Long applicationId, Long ownerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptApplication'");
    }

    @Override
    public void rejectApplication(Long projectId, Long applicationId, Long ownerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectApplication'");
    }

}
