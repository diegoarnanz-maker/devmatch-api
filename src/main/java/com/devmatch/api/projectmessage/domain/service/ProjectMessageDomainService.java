package com.devmatch.api.projectmessage.domain.service;

import com.devmatch.api.projectmessage.domain.model.ProjectMessage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio para la lógica de negocio de mensajes de proyecto.
 * Contiene reglas de negocio que no pertenecen a una entidad específica.
 */
@Service
public class ProjectMessageDomainService {
    
    private static final int MAX_MESSAGES_PER_HOUR = 50;
    private static final int MAX_MESSAGES_PER_DAY = 200;
    
    /**
     * Verifica si un usuario puede enviar un mensaje basado en límites de rate limiting
     */
    public boolean canUserSendMessage(Long userId, List<ProjectMessage> recentMessages) {
        if (recentMessages == null || recentMessages.isEmpty()) {
            return true;
        }
        
        // Verificar límite por hora
        long messagesLastHour = recentMessages.stream()
            .filter(msg -> msg.getSentAt().isAfter(java.time.LocalDateTime.now().minusHours(1)))
            .count();
        
        if (messagesLastHour >= MAX_MESSAGES_PER_HOUR) {
            return false;
        }
        
        // Verificar límite por día
        long messagesLastDay = recentMessages.stream()
            .filter(msg -> msg.getSentAt().isAfter(java.time.LocalDateTime.now().minusDays(1)))
            .count();
        
        return messagesLastDay < MAX_MESSAGES_PER_DAY;
    }
    
    /**
     * Verifica si un mensaje puede ser enviado en un proyecto
     */
    public boolean canSendMessageInProject(Long projectId) {
        // Aquí se pueden agregar más validaciones específicas del proyecto
        // Por ejemplo: verificar si el proyecto está activo, si el usuario es miembro, etc.
        
        return true;
    }
    
    /**
     * Calcula el tiempo de vida de un mensaje antes de ser archivado
     */
    public int getMessageArchiveDays() {
        return 30; // Los mensajes se archivan después de 30 días
    }
    
    /**
     * Verifica si un mensaje debe ser archivado automáticamente
     */
    public boolean shouldArchiveMessage(ProjectMessage message) {
        if (message.isDeleted()) {
            return false; // Los mensajes eliminados no se archivan
        }
        
        int archiveDays = getMessageArchiveDays();
        return message.getSentAt().isBefore(
            java.time.LocalDateTime.now().minusDays(archiveDays)
        );
    }
    
    /**
     * Valida si un usuario puede acceder a los mensajes de un proyecto
     */
    public boolean canUserAccessProjectMessages(Long userId, Long projectId) {
        // Aquí se implementaría la lógica para verificar si el usuario
        // es miembro del proyecto o tiene permisos para ver los mensajes
        
        // Por ahora, retornamos true, pero en una implementación real
        // se consultaría el servicio de proyectos para verificar membresía
        return true;
    }
}
