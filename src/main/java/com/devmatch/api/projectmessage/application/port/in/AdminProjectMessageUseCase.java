package com.devmatch.api.projectmessage.application.port.in;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Caso de uso para la gestión administrativa de mensajes de proyecto.
 * Permite a los administradores gestionar todos los mensajes del sistema.
 */
public interface AdminProjectMessageUseCase {
    
    /**
     * Obtiene todos los mensajes del sistema de forma paginada
     * @param pageable Configuración de paginación
     * @return Página de todos los mensajes
     */
    Page<ProjectMessageResponseDto> getAllMessages(Pageable pageable);
    
    /**
     * Obtiene todos los mensajes de un proyecto específico
     * @param projectId ID del proyecto
     * @param pageable Configuración de paginación
     * @return Página de mensajes del proyecto
     */
    Page<ProjectMessageResponseDto> getProjectMessages(Long projectId, Pageable pageable);
    
    /**
     * Obtiene todos los mensajes de un usuario específico
     * @param userId ID del usuario
     * @param pageable Configuración de paginación
     * @return Página de mensajes del usuario
     */
    Page<ProjectMessageResponseDto> getUserMessages(Long userId, Pageable pageable);
    
    /**
     * Busca mensajes con criterios administrativos
     * @param searchRequest DTO con los criterios de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de mensajes que coinciden con los criterios
     */
    Page<ProjectMessageResponseDto> searchMessages(ProjectMessageSearchRequestDto searchRequest, Pageable pageable);
    
    /**
     * Elimina un mensaje como administrador (eliminación física o lógica)
     * @param messageId ID del mensaje a eliminar
     */
    void deleteMessage(Long messageId);
    
    /**
     * Restaura un mensaje eliminado
     * @param messageId ID del mensaje a restaurar
     * @return DTO con los datos del mensaje restaurado
     */
    ProjectMessageResponseDto restoreMessage(Long messageId);
    
    /**
     * Obtiene estadísticas de mensajes del sistema
     * @return Estadísticas generales de mensajes
     */
    ProjectMessageStatsDto getMessageStats();
    
    /**
     * Obtiene los mensajes más activos (con más respuestas)
     * @param limit Número máximo de mensajes a retornar
     * @return Lista de mensajes más activos
     */
    List<ProjectMessageResponseDto> getMostActiveMessages(int limit);
    
    /**
     * Obtiene los usuarios más activos en mensajes
     * @param limit Número máximo de usuarios a retornar
     * @return Lista de usuarios más activos
     */
    List<UserMessageStatsDto> getMostActiveUsers(int limit);
    
    /**
     * DTO para estadísticas de mensajes
     */
    interface ProjectMessageStatsDto {
        long getTotalMessages();
        long getTotalActiveMessages();
        long getTotalDeletedMessages();
        long getMessagesToday();
        long getMessagesThisWeek();
        long getMessagesThisMonth();
    }
    
    /**
     * DTO para estadísticas de usuario
     */
    interface UserMessageStatsDto {
        Long getUserId();
        String getUsername();
        long getTotalMessages();
        long getMessagesThisMonth();
    }
}
