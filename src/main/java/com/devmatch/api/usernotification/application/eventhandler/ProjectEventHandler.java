package com.devmatch.api.usernotification.application.eventhandler;

// Importamos el evento que vamos a escuchar
import com.devmatch.api.project.domain.event.ProjectApplicationSubmittedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationAcceptedEvent;
import com.devmatch.api.project.domain.event.ProjectApplicationRejectedEvent;
import com.devmatch.api.project.domain.event.ProjectMemberJoinedEvent;

// Importamos el caso de uso que vamos a llamar
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;

// Importamos el repositorio para consultar miembros del proyecto
import com.devmatch.api.project.application.port.out.ProjectMemberRepositoryPort;
import com.devmatch.api.project.domain.model.ProjectMember;

// Importamos el caso de uso para consultar usuarios
import com.devmatch.api.user.application.port.in.UserQueryUseCase;

// Importamos utilidades de Java
import java.util.List;
import java.util.stream.Collectors;

// Anotaciones de Spring y Lombok
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Manejador de eventos que escucha eventos relacionados con proyectos.
 * 
 * ¿Qué hace?
 * - Escucha eventos de solicitudes de proyecto
 * - Crea notificaciones automáticamente
 * - No requiere intervención manual
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventHandler {
    
    // Inyectamos el caso de uso que crea notificaciones
    private final NotificationManagementUseCase notificationManagementUseCase;
    
    // Inyectamos el repositorio para consultar miembros del proyecto
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    
    // Inyectamos el caso de uso para consultar usuarios
    private final UserQueryUseCase userQueryUseCase;
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un ProjectApplicationSubmittedEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información de la solicitud
     */
    @EventListener
    public void handleProjectApplicationSubmitted(ProjectApplicationSubmittedEvent event) {
        // Log para debugging - vemos que llegó el evento
        log.info("Solicitud de proyecto enviada: usuario {} para proyecto {} ({}). Propietario: {}", 
                event.getApplicantId(), event.getProjectId(), event.getProjectName(), event.getOwnerId());
        
        try {
            // Llamamos al caso de uso para crear la notificación
            // La notificación se envía al SOLICITANTE para confirmar que su solicitud fue enviada
            notificationManagementUseCase.createProjectApplicationNotification(
                event.getApplicantId(), // Solicitante recibe la notificación
                event.getProjectId()
            );
            
            // Log de éxito
            log.info("Notificación de solicitud de proyecto creada exitosamente para el solicitante: {}", event.getApplicantId());
            
        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificación de solicitud de proyecto", e);
        }
    }
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un ProjectApplicationAcceptedEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información de la solicitud aceptada
     */
    @EventListener
    public void handleProjectApplicationAccepted(ProjectApplicationAcceptedEvent event) {
        // Log para debugging - vemos que llegó el evento
        log.info("Solicitud de proyecto aceptada: usuario {} para proyecto {} ({})", 
                event.getApplicantId(), event.getProjectId(), event.getProjectName());
        
        try {
            // Llamamos al caso de uso para crear la notificación
            // La notificación se envía al SOLICITANTE para confirmar que fue aceptado
            notificationManagementUseCase.createProjectApplicationAcceptedNotification(
                event.getApplicantId(), // Solicitante recibe la notificación de aceptación
                event.getProjectId()
            );
            
            // Log de éxito
            log.info("Notificación de solicitud aceptada creada exitosamente para el usuario: {}", event.getApplicantId());
            
        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificación de solicitud aceptada", e);
        }
    }
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un ProjectApplicationRejectedEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información de la solicitud rechazada
     */
    @EventListener
    public void handleProjectApplicationRejected(ProjectApplicationRejectedEvent event) {
        // Log para debugging - vemos que llegó el evento
        log.info("Solicitud de proyecto rechazada: usuario {} para proyecto {} ({})", 
                event.getApplicantId(), event.getProjectId(), event.getProjectName());
        
        try {
            // Llamamos al caso de uso para crear la notificación
            // La notificación se envía al SOLICITANTE para informarle que fue rechazado
            notificationManagementUseCase.createProjectApplicationRejectedNotification(
                event.getApplicantId(), // Solicitante recibe la notificación de rechazo
                event.getProjectId(),
                event.getProjectName()   // Nombre del proyecto
            );
            
            // Log de éxito
            log.info("Notificación de solicitud rechazada creada exitosamente para el usuario: {}", event.getApplicantId());
            
        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificación de solicitud rechazada", e);
        }
    }
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un ProjectMemberJoinedEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información del nuevo miembro
     */
    @EventListener
    public void handleProjectMemberJoined(ProjectMemberJoinedEvent event) {
        // Log para debugging - vemos que llegó el evento
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
