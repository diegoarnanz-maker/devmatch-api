package com.devmatch.api.usernotification.application.eventhandler;

import com.devmatch.api.projectreview.domain.event.ProjectReviewReceivedEvent;
import com.devmatch.api.projectreview.domain.event.ProjectReviewResponseEvent;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler para eventos relacionados con reviews de proyectos.
 * Maneja la creación automática de notificaciones para todos los eventos de reviews.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectReviewEventHandler {

    private final NotificationManagementUseCase notificationManagementUseCase;

    /**
     * Maneja el evento de review recibida.
     * Crea una notificación para el propietario del proyecto.
     */
    @EventListener
    public void handleProjectReviewReceived(ProjectReviewReceivedEvent event) {
        log.info("Review recibida: proyecto {} ({}). Propietario: {}. Revisor: {} ({})",
                event.getProjectId(), event.getProjectName(), event.getOwnerId(), 
                event.getReviewerName(), event.getReviewerId());
        
        try {
            notificationManagementUseCase.createProjectReviewReceivedNotification(
                event.getOwnerId(),        // El propietario recibe la notificación
                event.getProjectId(),      // ID del proyecto
                event.getReviewId(),       // ID de la review
                event.getReviewerName()    // Nombre del revisor
            );
            log.info("Notificación de review recibida creada exitosamente para el propietario: {}", event.getOwnerId());
            
        } catch (Exception e) {
            log.error("Error al crear notificación de review recibida", e);
        }
    }

    /**
     * Maneja el evento de respuesta a review.
     * Crea una notificación para el revisor original.
     */
    @EventListener
    public void handleProjectReviewResponse(ProjectReviewResponseEvent event) {
        log.info("Respuesta a review: proyecto {} ({}). Revisor: {} ({}). Propietario: {} ({})",
                event.getProjectId(), event.getProjectName(), 
                event.getReviewerName(), event.getReviewerId(),
                event.getOwnerName(), event.getOwnerId());
        
        try {
            notificationManagementUseCase.createProjectReviewResponseNotification(
                event.getReviewerId(),     // El revisor recibe la notificación
                event.getProjectId(),      // ID del proyecto
                event.getOwnerName()       // Nombre del propietario que respondió
            );
            log.info("Notificación de respuesta a review creada exitosamente para el revisor: {}", event.getReviewerId());
            
        } catch (Exception e) {
            log.error("Error al crear notificación de respuesta a review", e);
        }
    }
}
