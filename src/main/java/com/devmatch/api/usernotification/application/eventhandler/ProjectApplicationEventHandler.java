package com.devmatch.api.usernotification.application.eventhandler;

import com.devmatch.api.project.domain.event.ProjectApplicationCancelledEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationExpiredEvent;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler para eventos relacionados con aplicaciones a proyectos.
 * Maneja la creación automática de notificaciones para cancelaciones y expiraciones.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectApplicationEventHandler {

    private final NotificationManagementUseCase notificationManagementUseCase;

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
}
