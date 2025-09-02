package com.devmatch.api.projectmessage.application.eventhandler;

import com.devmatch.api.projectmessage.domain.event.ProjectMessageSentEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageReadEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageEditedEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Manejador de eventos para mensajes de proyecto.
 * Procesa los eventos de dominio relacionados con mensajes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectMessageEventHandler {
    
    /**
     * Maneja el evento de mensaje enviado
     */
    @EventListener
    public void handleMessageSentEvent(ProjectMessageSentEvent event) {
        log.info("Mensaje enviado - ID: {}, Proyecto: {}, Remitente: {}, Tipo: {}", 
                event.getMessageId(), event.getProjectId(), event.getSenderId(), event.getMessageType());
        
        // Aquí se pueden agregar acciones adicionales como:
        // - Enviar notificaciones push
        // - Actualizar contadores de actividad
        // - Disparar triggers de achievements
        // - Enviar emails de notificación
    }
    
    /**
     * Maneja el evento de mensaje leído
     */
    @EventListener
    public void handleMessageReadEvent(ProjectMessageReadEvent event) {
        log.info("Mensaje leído - ID: {}, Proyecto: {}, Lector: {}", 
                event.getMessageId(), event.getProjectId(), event.getReaderId());
        
        // Aquí se pueden agregar acciones adicionales como:
        // - Actualizar estadísticas de lectura
        // - Marcar notificaciones como leídas
        // - Disparar triggers de achievements por actividad
    }
    
    /**
     * Maneja el evento de mensaje editado
     */
    @EventListener
    public void handleMessageEditedEvent(ProjectMessageEditedEvent event) {
        log.info("Mensaje editado - ID: {}, Proyecto: {}, Editor: {}", 
                event.getMessageId(), event.getProjectId(), event.getEditorId());
        
        // Aquí se pueden agregar acciones adicionales como:
        // - Registrar historial de cambios
        // - Notificar a otros miembros sobre la edición
        // - Actualizar índices de búsqueda
    }
    
    /**
     * Maneja el evento de mensaje eliminado
     */
    @EventListener
    public void handleMessageDeletedEvent(ProjectMessageDeletedEvent event) {
        log.info("Mensaje eliminado - ID: {}, Proyecto: {}, Eliminador: {}", 
                event.getMessageId(), event.getProjectId(), event.getDeleterId());
        
        // Aquí se pueden agregar acciones adicionales como:
        // - Limpiar referencias huérfanas
        // - Actualizar contadores
        // - Registrar auditoría de eliminación
    }
}
