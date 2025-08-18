package com.devmatch.api.usernotification.application.eventhandler;

import com.devmatch.api.project.domain.event.ProjectApplicationCancelledEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationExpiredEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationSubmittedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationAcceptedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationRejectedEvent;
import com.devmatch.api.project.domain.event.ProjectMemberJoinedEvent;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import com.devmatch.api.project.application.port.out.ProjectMemberRepositoryPort;
import com.devmatch.api.project.domain.model.ProjectMember;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Event handler para eventos relacionados con aplicaciones a proyectos.
 * Maneja la creación automática de notificaciones para todos los eventos de aplicaciones.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectApplicationEventHandler {

    private final NotificationManagementUseCase notificationManagementUseCase;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    private final UserQueryUseCase userQueryUseCase;

    /**
     * Maneja el evento de aplicación enviada.
     * Crea notificaciones para el solicitante y para el propietario del proyecto.
     */
    @EventListener
    public void handleProjectApplicationSubmitted(ProjectApplicationSubmittedEvent event) {
        log.info("Aplicación enviada: usuario {} para proyecto {} ({}). Propietario: {}",
                event.getApplicantId(), event.getProjectId(), event.getProjectName(), event.getOwnerId());
        
        try {
            // 1. Notificación para el SOLICITANTE (confirmación)
            notificationManagementUseCase.createProjectApplicationNotification(
                event.getApplicantId(), // El solicitante recibe la notificación
                event.getProjectId()
            );
            log.info("Notificación de aplicación enviada creada exitosamente para el solicitante: {}", event.getApplicantId());
            
            // 2. Notificación para el PROPIETARIO (nueva solicitud)
            notificationManagementUseCase.createProjectApplicationReceivedNotification(
                event.getOwnerId(), // El propietario recibe la notificación
                event.getProjectId(),
                event.getApplicantId() // ID del solicitante para obtener su nombre
            );
            log.info("Notificación de nueva solicitud creada exitosamente para el propietario: {}", event.getOwnerId());
            
        } catch (Exception e) {
            log.error("Error al crear notificaciones de aplicación enviada", e);
        }
    }

    /**
     * Maneja el evento de aplicación aceptada.
     * Crea una notificación para el usuario que fue aceptado.
     */
    @EventListener
    public void handleProjectApplicationAccepted(ProjectApplicationAcceptedEvent event) {
        log.info("Aplicación aceptada: usuario {} para proyecto {} ({})",
                event.getApplicantId(), event.getProjectId(), event.getProjectName());
        try {
            notificationManagementUseCase.createProjectApplicationAcceptedNotification(
                event.getApplicantId(),
                event.getProjectId()
            );
            log.info("Notificación de aplicación aceptada creada exitosamente para el usuario: {}", event.getApplicantId());
        } catch (Exception e) {
            log.error("Error al crear notificación de aplicación aceptada", e);
        }
    }

    /**
     * Maneja el evento de aplicación rechazada.
     * Crea una notificación para el usuario que fue rechazado.
     */
    @EventListener
    public void handleProjectApplicationRejected(ProjectApplicationRejectedEvent event) {
        log.info("Aplicación rechazada: usuario {} para proyecto {} ({})",
                event.getApplicantId(), event.getProjectId(), event.getProjectName());
        try {
            notificationManagementUseCase.createProjectApplicationRejectedNotification(
                event.getApplicantId(),
                event.getProjectId(),
                null // Sin razón específica por ahora
            );
            log.info("Notificación de aplicación rechazada creada exitosamente para el usuario: {}", event.getApplicantId());
        } catch (Exception e) {
            log.error("Error al crear notificación de aplicación rechazada", e);
        }
    }

    /**
     * Maneja el evento de aplicación cancelada.
     * Crea una notificación para el usuario que canceló la aplicación.
     */
    @EventListener
    public void handleProjectApplicationCancelled(ProjectApplicationCancelledEvent event) {
        log.info("Aplicación cancelada: usuario {} para proyecto {} ({})",
                event.getApplicantId(), event.getProjectId(), event.getProjectName());

        try {
            notificationManagementUseCase.createProjectApplicationCancelledNotification(
                event.getApplicantId(),
                event.getProjectId()
            );
            log.info("Notificación de aplicación cancelada creada exitosamente para el usuario: {}", event.getApplicantId());
        } catch (Exception e) {
            log.error("Error al crear notificación de aplicación cancelada", e);
        }
    }

    /**
     * Maneja el evento de aplicación expirada.
     * Crea una notificación para el usuario cuya aplicación expiró automáticamente.
     */
    @EventListener
    public void handleProjectApplicationExpired(ProjectApplicationExpiredEvent event) {
        log.info("Aplicación expirada: usuario {} para proyecto {} ({})",
                event.getApplicantId(), event.getProjectId(), event.getProjectName());

        try {
            notificationManagementUseCase.createProjectApplicationExpiredNotification(
                event.getApplicantId(),
                event.getProjectId()
            );
            log.info("Notificación de aplicación expirada creada exitosamente para el usuario: {}", event.getApplicantId());
        } catch (Exception e) {
            log.error("Error al crear notificación de aplicación expirada", e);
        }
    }

    /**
     * Maneja el evento de nuevo miembro unido al proyecto.
     * Crea notificaciones para todos los miembros existentes.
     */
    @EventListener
    public void handleProjectMemberJoined(ProjectMemberJoinedEvent event) {
        log.info("Nuevo miembro unido al proyecto: usuario {} con rol {} para proyecto {} ({})",
                event.getNewMemberId(), event.getNewMemberRole(), event.getProjectId(), event.getProjectName());

        try {
            // 1. Obtener TODOS los miembros del proyecto
            List<ProjectMember> existingMembers = projectMemberRepositoryPort.getActiveMembersByProjectId(event.getProjectId());

            // 2. Filtrar para NO notificar al propio nuevo miembro
            List<ProjectMember> membersToNotify = existingMembers.stream()
                .filter(member -> !member.getUserId().equals(event.getNewMemberId()))
                .collect(Collectors.toList());

            log.info("Notificando a {} miembros existentes sobre el nuevo miembro {}",
                    membersToNotify.size(), event.getNewMemberId());

            // 3. Crear notificación para CADA miembro existente
            for (ProjectMember member : membersToNotify) {
                try {
                    // Obtener el nombre del nuevo miembro
                    String newMemberName = userQueryUseCase.findUserById(event.getNewMemberId()).getUsername();

                    notificationManagementUseCase.createProjectMemberJoinedNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        newMemberName              // Nombre del nuevo miembro
                    );

                    log.debug("Notificación enviada al miembro {} sobre nuevo miembro {}",
                            member.getUserId(), newMemberName);

                } catch (Exception e) {
                    log.warn("No se pudo obtener el nombre del usuario {}, usando ID como fallback", event.getNewMemberId());

                    // Fallback: usar el ID como nombre
                    notificationManagementUseCase.createProjectMemberJoinedNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        "Usuario " + event.getNewMemberId()  // ID como nombre de fallback
                    );
                }
            }

            // Log de éxito
            log.info("Notificaciones de nuevo miembro enviadas exitosamente a {} miembros del proyecto {}",
                    membersToNotify.size(), event.getProjectId());

        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificaciones de nuevo miembro", e);
        }
    }
}
