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
import com.devmatch.api.project.domain.model.valueobject.MotivationMessage;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.project.domain.event.ProjectApplicationSubmittedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationAcceptedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationRejectedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationCancelledEvent;
import com.devmatch.api.project.domain.event.ProjectMemberJoinedEvent;
import com.devmatch.api.shared.application.port.out.DomainEventPublisher;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para gestión de aplicaciones a proyectos.
 * 
 * <p>Este servicio gestiona todo el ciclo de vida de las aplicaciones de usuarios
 * a proyectos, desde la solicitud inicial hasta la resolución final por parte
 * del propietario del proyecto.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Procesar solicitudes de aplicación a proyectos con validaciones.</li>
 *   <li>Gestionar el flujo de aprobación/rechazo de aplicaciones.</li>
 *   <li>Permitir la cancelación de aplicaciones por parte del solicitante.</li>
 *   <li>Proporcionar consultas de aplicaciones por proyecto y usuario.</li>
 *   <li>Integrar automáticamente usuarios aceptados como miembros del proyecto.</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Los usuarios solicitan unirse a proyectos con mensajes de motivación.</li>
 *   <li>Se validan las condiciones del proyecto y el estado del usuario.</li>
 *   <li>Los propietarios revisan y deciden sobre las aplicaciones.</li>
 *   <li>Las aplicaciones aceptadas resultan en la integración del usuario al equipo.</li>
 *   <li>Se publican eventos de dominio para notificar cambios de estado.</li>
 * </ol>
 * 
 * <h3>Consideraciones de negocio:</h3>
 * <ul>
 *   <li>Todas las operaciones están transaccionalmente gestionadas.</li>
 *   <li>Se aplican validaciones estrictas de permisos y estados.</li>
 *   <li>Se previene la aplicación duplicada del mismo usuario.</li>
 *   <li>Se integran automáticamente usuarios aceptados como miembros.</li>
 *   <li>Se publican eventos para notificar cambios a otros módulos.</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectApplicationUseCaseImpl implements ProjectApplicationUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    private final ProjectApplicationRepositoryPort projectApplicationRepositoryPort;
    private final ProjectApplicationMapper projectApplicationMapper;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void applyToProject(Long projectId, Long userId, String motivationMessage) {
        // Validar proyecto y usuario
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));

        // Validar condiciones del proyecto
        if (!project.isOpenForApplications()) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " no está abierto para aplicaciones");
        }

        int currentTeamSize = projectMemberRepositoryPort.countActiveMembersByProjectId(projectId);
        if (project.isFull(currentTeamSize)) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " ya está lleno");
        }

        if (project.isOwner(userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " no puede aplicar al proyecto");
        }

        if (projectApplicationRepositoryPort.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " ya ha aplicado al proyecto con ID " + projectId);
        }

        // Crear y guardar aplicación
        MotivationMessage validatedMessage = new MotivationMessage(motivationMessage);
        ProjectApplication application = new ProjectApplication(projectId, userId, validatedMessage);
        
        projectApplicationRepositoryPort.save(application);
        
        // Notificar evento
        domainEventPublisher.publish(new ProjectApplicationSubmittedEvent(
            project.getOwnerId(),
            projectId,
            project.getTitle().getValue(),
            userId
        ));
    }

    @Override
    public List<ProjectApplicationResponseDto> getProjectApplications(Long projectId, Long ownerId) {
        // Validar proyecto y permisos
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        // Obtener y marcar aplicaciones como vistas
        List<ProjectApplication> applications = projectApplicationRepositoryPort.findByProjectId(projectId);
        
        List<ProjectApplication> updatedApplications = applications.stream()
                .map(application -> {
                    if (application.isSeenByOwner()) {
                        return application;
                    }
                    ProjectApplication markedApplication = application.markAsSeen();
                    return projectApplicationRepositoryPort.save(markedApplication);
                })
                .toList();
        
        // Convertir a DTOs
        return projectApplicationMapper.toResponseDtoList(updatedApplications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectApplicationResponseDto> getUserApplications(Long userId) {
        // Validar usuario y obtener aplicaciones
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
        
        List<ProjectApplication> applications = projectApplicationRepositoryPort.findByUserId(userId);
        
        // Convertir a DTOs
        return projectApplicationMapper.toResponseDtoList(applications);
    }

    @Override
    public void acceptApplication(Long projectId, Long applicationId, Long ownerId) {
        // Validar proyecto, permisos y aplicación
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        if (!application.getProjectId().equals(projectId)) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no pertenece al proyecto con ID " + projectId);
        }
        
        if (!application.isPending()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " ya no está pendiente");
        }
        
        int currentTeamSize = projectMemberRepositoryPort.countActiveMembersByProjectId(projectId);
        if (project.isFull(currentTeamSize)) {
            throw new ProjectOperationNotAllowedException(
                    "El proyecto con ID " + projectId + " ya está lleno");
        }
        
        // Aceptar aplicación y agregar como miembro
        ProjectApplication acceptedApplication = application.accept();
        
        projectApplicationRepositoryPort.save(acceptedApplication);
        
        projectMemberRepositoryPort.addMember(projectId, acceptedApplication.getUserId(), "DEVELOPER", false);
        
        // Notificar eventos
        domainEventPublisher.publish(new ProjectApplicationAcceptedEvent(
            acceptedApplication.getUserId(),
            projectId,
            project.getTitle().getValue()
        ));
        
        domainEventPublisher.publish(new ProjectMemberJoinedEvent(
            acceptedApplication.getUserId(),
            projectId,
            project.getTitle().getValue(),
            "DEVELOPER"
        ));
    }

    @Override
    public void rejectApplication(Long projectId, Long applicationId, Long ownerId) {
        // Validar proyecto, permisos y aplicación
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        
        if (!project.isOwner(ownerId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + ownerId + " no es el propietario del proyecto con ID " + projectId);
        }
        
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        if (!application.getProjectId().equals(projectId)) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no pertenece al proyecto con ID " + projectId);
        }
        
        if (!application.isPending()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " ya no está pendiente");
        }
        
        // Rechazar aplicación y notificar
        ProjectApplication rejectedApplication = application.reject();
        
        projectApplicationRepositoryPort.save(rejectedApplication);
        
        // Notificar evento
        domainEventPublisher.publish(new ProjectApplicationRejectedEvent(
            rejectedApplication.getUserId(),
            projectId,
            project.getTitle().getValue()
        ));
    }

    @Override
    public void cancelApplication(Long applicationId, Long userId) {
        // Validar aplicación y permisos
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
                .orElseThrow(() -> new ProjectOperationNotAllowedException(
                        "Aplicación con ID " + applicationId + " no encontrada"));
        
        if (!application.getUserId().equals(userId)) {
            throw new ProjectOperationNotAllowedException(
                    "El usuario con ID " + userId + " no puede cancelar la aplicación con ID " + applicationId);
        }
        
        if (!application.canBeCancelled()) {
            throw new ProjectOperationNotAllowedException(
                    "La aplicación con ID " + applicationId + " no puede ser cancelada");
        }
        
        // Cancelar aplicación y notificar
        ProjectApplication cancelledApplication = application.cancel();
        
        projectApplicationRepositoryPort.save(cancelledApplication);
        
        // Obtener proyecto para evento
        Project project = projectRepositoryPort.findById(application.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(application.getProjectId()));
        
        // Notificar evento
        domainEventPublisher.publish(new ProjectApplicationCancelledEvent(
            cancelledApplication.getUserId(),
            application.getProjectId(),
            project.getTitle().getValue(),
            project.getOwnerId()
        ));
    }

}
