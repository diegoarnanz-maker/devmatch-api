package com.devmatch.api.usernotification.application.eventhandler;

import com.devmatch.api.projectmessage.domain.event.ProjectMessageSentEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageReadEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageEditedEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageDeletedEvent;
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
 * Event handler para eventos relacionados con mensajes de proyecto.
 * Maneja la creación automática de notificaciones para eventos de mensajes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMessageNotificationEventHandler {

    private final NotificationManagementUseCase notificationManagementUseCase;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;
    private final UserQueryUseCase userQueryUseCase;

    /**
     * Maneja el evento de mensaje enviado.
     * Crea notificaciones para todos los miembros del proyecto excepto el remitente.
     */
    @EventListener
    public void handleProjectMessageSent(ProjectMessageSentEvent event) {
        log.info("Mensaje enviado: ID {} en proyecto {} por usuario {} (tipo: {})",
                event.getMessageId(), event.getProjectId(), event.getSenderId(), event.getMessageType());
        
        try {
            // 1. Obtener TODOS los miembros del proyecto
            List<ProjectMember> projectMembers = projectMemberRepositoryPort.getActiveMembersByProjectId(event.getProjectId());

            // 2. Filtrar para NO notificar al remitente
            List<ProjectMember> membersToNotify = projectMembers.stream()
                .filter(member -> !member.getUserId().equals(event.getSenderId()))
                .collect(Collectors.toList());

            log.info("Notificando a {} miembros sobre nuevo mensaje en proyecto {}",
                    membersToNotify.size(), event.getProjectId());

            // 3. Crear notificación para CADA miembro
            for (ProjectMember member : membersToNotify) {
                try {
                    // Obtener el nombre del remitente
                    String senderName = userQueryUseCase.findUserById(event.getSenderId()).getUsername();

                    // Crear notificación personalizada según el tipo de mensaje
                    String message = createMessageNotificationText(event.getMessageType(), senderName);
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje personalizado
                    );

                    log.debug("Notificación de mensaje enviada al miembro {} sobre mensaje de {}",
                            member.getUserId(), senderName);

                } catch (Exception e) {
                    log.warn("No se pudo obtener el nombre del usuario {}, usando ID como fallback", event.getSenderId());

                    // Fallback: usar el ID como nombre
                    String message = createMessageNotificationText(event.getMessageType(), "Usuario " + event.getSenderId());
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje de fallback
                    );
                }
            }

            // Log de éxito
            log.info("Notificaciones de mensaje enviadas exitosamente a {} miembros del proyecto {}",
                    membersToNotify.size(), event.getProjectId());

        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificaciones de mensaje enviado", e);
        }
    }

    /**
     * Maneja el evento de mensaje leído.
     * No crea notificaciones adicionales, solo registra la actividad.
     */
    @EventListener
    public void handleProjectMessageRead(ProjectMessageReadEvent event) {
        log.info("Mensaje leído: ID {} en proyecto {} por usuario {}",
                event.getMessageId(), event.getProjectId(), event.getReaderId());
        
        // No se crean notificaciones para mensajes leídos
        // Solo se registra la actividad para estadísticas
    }

    /**
     * Maneja el evento de mensaje editado.
     * Crea notificaciones para todos los miembros del proyecto excepto el editor.
     */
    @EventListener
    public void handleProjectMessageEdited(ProjectMessageEditedEvent event) {
        log.info("Mensaje editado: ID {} en proyecto {} por usuario {}",
                event.getMessageId(), event.getProjectId(), event.getEditorId());
        
        try {
            // 1. Obtener TODOS los miembros del proyecto
            List<ProjectMember> projectMembers = projectMemberRepositoryPort.getActiveMembersByProjectId(event.getProjectId());

            // 2. Filtrar para NO notificar al editor
            List<ProjectMember> membersToNotify = projectMembers.stream()
                .filter(member -> !member.getUserId().equals(event.getEditorId()))
                .collect(Collectors.toList());

            log.info("Notificando a {} miembros sobre mensaje editado en proyecto {}",
                    membersToNotify.size(), event.getProjectId());

            // 3. Crear notificación para CADA miembro
            for (ProjectMember member : membersToNotify) {
                try {
                    // Obtener el nombre del editor
                    String editorName = userQueryUseCase.findUserById(event.getEditorId()).getUsername();

                    String message = String.format("📝 %s editó un mensaje en el proyecto", editorName);
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje de edición
                    );

                    log.debug("Notificación de mensaje editado enviada al miembro {} sobre edición de {}",
                            member.getUserId(), editorName);

                } catch (Exception e) {
                    log.warn("No se pudo obtener el nombre del usuario {}, usando ID como fallback", event.getEditorId());

                    // Fallback: usar el ID como nombre
                    String message = String.format("📝 Usuario %d editó un mensaje en el proyecto", event.getEditorId());
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje de fallback
                    );
                }
            }

            // Log de éxito
            log.info("Notificaciones de mensaje editado enviadas exitosamente a {} miembros del proyecto {}",
                    membersToNotify.size(), event.getProjectId());

        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificaciones de mensaje editado", e);
        }
    }

    /**
     * Maneja el evento de mensaje eliminado.
     * Crea notificaciones para todos los miembros del proyecto excepto el eliminador.
     */
    @EventListener
    public void handleProjectMessageDeleted(ProjectMessageDeletedEvent event) {
        log.info("Mensaje eliminado: ID {} en proyecto {} por usuario {}",
                event.getMessageId(), event.getProjectId(), event.getDeleterId());
        
        try {
            // 1. Obtener TODOS los miembros del proyecto
            List<ProjectMember> projectMembers = projectMemberRepositoryPort.getActiveMembersByProjectId(event.getProjectId());

            // 2. Filtrar para NO notificar al eliminador
            List<ProjectMember> membersToNotify = projectMembers.stream()
                .filter(member -> !member.getUserId().equals(event.getDeleterId()))
                .collect(Collectors.toList());

            log.info("Notificando a {} miembros sobre mensaje eliminado en proyecto {}",
                    membersToNotify.size(), event.getProjectId());

            // 3. Crear notificación para CADA miembro
            for (ProjectMember member : membersToNotify) {
                try {
                    // Obtener el nombre del eliminador
                    String deleterName = userQueryUseCase.findUserById(event.getDeleterId()).getUsername();

                    String message = String.format("🗑️ %s eliminó un mensaje en el proyecto", deleterName);
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje de eliminación
                    );

                    log.debug("Notificación de mensaje eliminado enviada al miembro {} sobre eliminación de {}",
                            member.getUserId(), deleterName);

                } catch (Exception e) {
                    log.warn("No se pudo obtener el nombre del usuario {}, usando ID como fallback", event.getDeleterId());

                    // Fallback: usar el ID como nombre
                    String message = String.format("🗑️ Usuario %d eliminó un mensaje en el proyecto", event.getDeleterId());
                    
                    notificationManagementUseCase.createSystemMessageNotification(
                        member.getUserId(),        // Miembro que recibe la notificación
                        event.getProjectId(),      // ID del proyecto
                        message                    // Mensaje de fallback
                    );
                }
            }

            // Log de éxito
            log.info("Notificaciones de mensaje eliminado enviadas exitosamente a {} miembros del proyecto {}",
                    membersToNotify.size(), event.getProjectId());

        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificaciones de mensaje eliminado", e);
        }
    }

    /**
     * Crea el texto de notificación personalizado según el tipo de mensaje.
     */
    private String createMessageNotificationText(String messageType, String senderName) {
        switch (messageType.toUpperCase()) {
            case "ANNOUNCEMENT":
                return String.format("📢 %s publicó un anuncio en el proyecto", senderName);
            case "TASK_UPDATE":
                return String.format("✅ %s actualizó una tarea en el proyecto", senderName);
            case "MEETING_REMINDER":
                return String.format("⏰ %s envió un recordatorio de reunión", senderName);
            case "FILE_SHARE":
                return String.format("📎 %s compartió un archivo en el proyecto", senderName);
            case "CODE_REVIEW":
                return String.format("🔍 %s envió una revisión de código", senderName);
            case "SYSTEM":
                return String.format("🔧 %s envió un mensaje del sistema", senderName);
            case "GENERAL":
            case "TEXT":
            default:
                return String.format("💬 %s envió un mensaje en el proyecto", senderName);
        }
    }
}