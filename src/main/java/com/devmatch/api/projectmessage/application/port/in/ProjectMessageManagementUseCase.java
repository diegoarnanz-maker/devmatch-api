package com.devmatch.api.projectmessage.application.port.in;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Caso de uso para la gestión de mensajes de proyecto.
 * Permite a los usuarios enviar, editar, eliminar y consultar mensajes en proyectos.
 */
public interface ProjectMessageManagementUseCase {
    
    /**
     * Envía un nuevo mensaje en un proyecto
     * @param userId ID del usuario que envía el mensaje
     * @param request DTO con los datos del mensaje
     * @return DTO con los datos del mensaje enviado
     */
    ProjectMessageResponseDto sendMessage(Long userId, ProjectMessageRequestDto request);
    
    /**
     * Responde a un mensaje específico en un proyecto
     * @param userId ID del usuario que responde
     * @param replyToMessageId ID del mensaje al que se responde
     * @param request DTO con los datos de la respuesta
     * @return DTO con los datos del mensaje de respuesta enviado
     */
    ProjectMessageResponseDto replyToMessage(Long userId, Long replyToMessageId, ProjectMessageRequestDto request);
    
    /**
     * Edita el contenido de un mensaje existente
     * @param userId ID del usuario que edita el mensaje
     * @param messageId ID del mensaje a editar
     * @param request DTO con el nuevo contenido
     * @return DTO con los datos del mensaje editado
     */
    ProjectMessageResponseDto editMessage(Long userId, Long messageId, ProjectMessageUpdateRequestDto request);
    
    /**
     * Elimina un mensaje (soft delete)
     * @param userId ID del usuario que elimina el mensaje
     * @param messageId ID del mensaje a eliminar
     */
    void deleteMessage(Long userId, Long messageId);
    
    /**
     * Marca un mensaje como leído
     * @param userId ID del usuario que lee el mensaje
     * @param messageId ID del mensaje a marcar como leído
     * @return DTO con los datos del mensaje actualizado
     */
    ProjectMessageResponseDto markMessageAsRead(Long userId, Long messageId);
    
    /**
     * Obtiene un mensaje específico por su ID
     * @param messageId ID del mensaje
     * @param userId ID del usuario que solicita el mensaje
     * @return DTO con los datos del mensaje
     */
    ProjectMessageResponseDto getMessageById(Long messageId, Long userId);
    
    /**
     * Obtiene todos los mensajes de un proyecto de forma paginada
     * @param projectId ID del proyecto
     * @param userId ID del usuario que solicita los mensajes
     * @param pageable Configuración de paginación
     * @return Página de mensajes del proyecto
     */
    Page<ProjectMessageResponseDto> getProjectMessages(Long projectId, Long userId, Pageable pageable);
    
    /**
     * Busca mensajes con criterios específicos
     * @param searchRequest DTO con los criterios de búsqueda
     * @param userId ID del usuario que realiza la búsqueda
     * @param pageable Configuración de paginación
     * @return Página de mensajes que coinciden con los criterios
     */
    Page<ProjectMessageResponseDto> searchMessages(ProjectMessageSearchRequestDto searchRequest, Long userId, Pageable pageable);
    
    /**
     * Obtiene los mensajes no leídos de un usuario en un proyecto
     * @param projectId ID del proyecto
     * @param userId ID del usuario
     * @return Lista de mensajes no leídos
     */
    List<ProjectMessageResponseDto> getUnreadMessages(Long projectId, Long userId);
    
    /**
     * Marca todos los mensajes de un proyecto como leídos para un usuario
     * @param projectId ID del proyecto
     * @param userId ID del usuario
     */
    void markAllMessagesAsRead(Long projectId, Long userId);
    
    /**
     * Obtiene el historial de mensajes de un hilo de conversación
     * @param rootMessageId ID del mensaje raíz del hilo
     * @param userId ID del usuario que solicita el historial
     * @return Lista de mensajes del hilo ordenados cronológicamente
     */
    List<ProjectMessageResponseDto> getMessageThread(Long rootMessageId, Long userId);
}
