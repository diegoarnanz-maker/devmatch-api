package com.devmatch.api.projectmessage.application.service;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageUpdateRequestDto;
import com.devmatch.api.projectmessage.application.mapper.ProjectMessageMapper;
import com.devmatch.api.projectmessage.application.port.in.ProjectMessageManagementUseCase;
import com.devmatch.api.projectmessage.application.port.out.ProjectMessageEventPublisher;
import com.devmatch.api.projectmessage.application.port.out.ProjectMessageRepository;
import com.devmatch.api.projectmessage.application.port.out.ProjectService;
import com.devmatch.api.projectmessage.application.port.out.UserService;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageDeletedEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageEditedEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageReadEvent;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageSentEvent;
import com.devmatch.api.projectmessage.domain.exception.ProjectMessageLimitExceededException;
import com.devmatch.api.projectmessage.domain.exception.ProjectMessageNotFoundException;
import com.devmatch.api.projectmessage.domain.exception.ProjectMessageOperationNotAllowedException;
import com.devmatch.api.projectmessage.domain.model.ProjectMessage;
import com.devmatch.api.projectmessage.domain.model.valueobject.MessageContent;
import com.devmatch.api.projectmessage.domain.service.ProjectMessageDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para la gestión de mensajes de proyecto.
 * Contiene la lógica de aplicación para operaciones de mensajes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectMessageManagementService implements ProjectMessageManagementUseCase {
    
    private final ProjectMessageRepository projectMessageRepository;
    private final ProjectMessageMapper projectMessageMapper;
    private final ProjectMessageDomainService projectMessageDomainService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMessageEventPublisher eventPublisher;
    
    @Override
    public ProjectMessageResponseDto sendMessage(Long userId, ProjectMessageRequestDto request) {
        log.info("Enviando mensaje para usuario {} en proyecto {}", userId, request.getProjectId());
        
        // Validaciones de negocio
        validateUserCanSendMessage(userId, request.getProjectId());
        
        // Verificar límites de rate limiting
        validateRateLimiting(userId);
        
        // Crear mensaje
        ProjectMessage message = projectMessageMapper.toDomain(request, userId);
        
        // Guardar mensaje
        ProjectMessage savedMessage = projectMessageRepository.save(message);
        
        // Publicar evento
        ProjectMessageSentEvent event = new ProjectMessageSentEvent(
            this, savedMessage.getId(), savedMessage.getProjectId(), 
            savedMessage.getSenderId(), savedMessage.getType().getValue()
        );
        eventPublisher.publishMessageSentEvent(event);
        
        log.info("Mensaje {} enviado exitosamente", savedMessage.getId());
        
        return projectMessageMapper.toResponseDto(savedMessage);
    }
    
    @Override
    public ProjectMessageResponseDto editMessage(Long userId, Long messageId, ProjectMessageUpdateRequestDto request) {
        log.info("Editando mensaje {} por usuario {}", messageId, userId);
        
        // Buscar mensaje
        ProjectMessage message = findMessageById(messageId);
        
        // Validar permisos
        if (!message.canBeEditedBy(userId)) {
            throw new ProjectMessageOperationNotAllowedException("No tienes permisos para editar este mensaje");
        }
        
        // Crear nuevo contenido
        MessageContent newContent = projectMessageMapper.toMessageContent(request);
        
        // Editar mensaje
        ProjectMessage editedMessage = message.editContent(newContent);
        
        // Guardar cambios
        ProjectMessage savedMessage = projectMessageRepository.save(editedMessage);
        
        // Publicar evento
        ProjectMessageEditedEvent event = new ProjectMessageEditedEvent(
            this, savedMessage.getId(), savedMessage.getProjectId(), 
            userId, message.getContent().getValue(), savedMessage.getContent().getValue()
        );
        eventPublisher.publishMessageEditedEvent(event);
        
        log.info("Mensaje {} editado exitosamente", messageId);
        
        return projectMessageMapper.toResponseDto(savedMessage);
    }
    
    @Override
    public void deleteMessage(Long userId, Long messageId) {
        log.info("Eliminando mensaje {} por usuario {}", messageId, userId);
        
        // Buscar mensaje
        ProjectMessage message = findMessageById(messageId);
        
        // Validar permisos
        if (!message.canBeDeletedBy(userId)) {
            throw new ProjectMessageOperationNotAllowedException("No tienes permisos para eliminar este mensaje");
        }
        
        // Eliminar mensaje (soft delete)
        ProjectMessage deletedMessage = message.delete();
        
        // Guardar cambios
        projectMessageRepository.save(deletedMessage);
        
        // Publicar evento
        ProjectMessageDeletedEvent event = new ProjectMessageDeletedEvent(
            this, deletedMessage.getId(), deletedMessage.getProjectId(), userId
        );
        eventPublisher.publishMessageDeletedEvent(event);
        
        log.info("Mensaje {} eliminado exitosamente", messageId);
    }
    
    @Override
    public ProjectMessageResponseDto markMessageAsRead(Long userId, Long messageId) {
        log.info("Marcando mensaje {} como leído por usuario {}", messageId, userId);
        
        // Buscar mensaje
        ProjectMessage message = findMessageById(messageId);
        
        // Validar que el usuario puede acceder al mensaje
        if (!projectService.isUserMemberOfProject(userId, message.getProjectId())) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este mensaje");
        }
        
        // Publicar evento de lectura
        ProjectMessageReadEvent event = new ProjectMessageReadEvent(
            this, message.getId(), message.getProjectId(), userId
        );
        eventPublisher.publishMessageReadEvent(event);
        
        log.info("Mensaje {} marcado como leído", messageId);
        
        return projectMessageMapper.toResponseDto(message);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectMessageResponseDto getMessageById(Long messageId, Long userId) {
        log.info("Obteniendo mensaje {} para usuario {}", messageId, userId);
        
        ProjectMessage message = findMessageById(messageId);
        
        // Validar acceso
        if (!projectService.isUserMemberOfProject(userId, message.getProjectId())) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este mensaje");
        }
        
        return projectMessageMapper.toResponseDto(message);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> getProjectMessages(Long projectId, Long userId, Pageable pageable) {
        log.info("Obteniendo mensajes del proyecto {} para usuario {}", projectId, userId);
        
        // Validar acceso al proyecto
        if (!projectService.isUserMemberOfProject(userId, projectId)) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este proyecto");
        }
        
        Page<ProjectMessage> messages = projectMessageRepository.findByProjectId(projectId, pageable);
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> searchMessages(ProjectMessageSearchRequestDto searchRequest, Long userId, Pageable pageable) {
        log.info("Buscando mensajes con criterios para usuario {}", userId);
        
        // Validar acceso al proyecto si se especifica
        if (searchRequest.getProjectId() != null) {
            if (!projectService.isUserMemberOfProject(userId, searchRequest.getProjectId())) {
                throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este proyecto");
            }
        }
        
        Page<ProjectMessage> messages = projectMessageRepository.findByCriteria(
            searchRequest.getProjectId(),
            searchRequest.getSenderId(),
            searchRequest.getMessageType(),
            searchRequest.getFromDate(),
            searchRequest.getToDate(),
            searchRequest.getIncludeDeleted() != null ? searchRequest.getIncludeDeleted() : false,
            pageable
        );
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectMessageResponseDto> getUnreadMessages(Long projectId, Long userId) {
        log.info("Obteniendo mensajes no leídos del proyecto {} para usuario {}", projectId, userId);
        
        // Validar acceso al proyecto
        if (!projectService.isUserMemberOfProject(userId, projectId)) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este proyecto");
        }
        
        List<ProjectMessage> unreadMessages = projectMessageRepository.findUnreadByProjectAndUser(projectId, userId);
        
        return unreadMessages.stream()
            .map(projectMessageMapper::toResponseDto)
            .collect(Collectors.toList());
    }
    
    @Override
    public void markAllMessagesAsRead(Long projectId, Long userId) {
        log.info("Marcando todos los mensajes del proyecto {} como leídos para usuario {}", projectId, userId);
        
        // Validar acceso al proyecto
        if (!projectService.isUserMemberOfProject(userId, projectId)) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este proyecto");
        }
        
        // Obtener mensajes no leídos
        List<ProjectMessage> unreadMessages = projectMessageRepository.findUnreadByProjectAndUser(projectId, userId);
        
        // Marcar cada mensaje como leído
        for (ProjectMessage message : unreadMessages) {
            ProjectMessageReadEvent event = new ProjectMessageReadEvent(
                this, message.getId(), message.getProjectId(), userId
            );
            eventPublisher.publishMessageReadEvent(event);
        }
        
        log.info("Todos los mensajes del proyecto {} marcados como leídos", projectId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectMessageResponseDto> getMessageThread(Long rootMessageId, Long userId) {
        log.info("Obteniendo hilo de conversación para mensaje {} por usuario {}", rootMessageId, userId);
        
        // Buscar mensaje raíz
        ProjectMessage rootMessage = findMessageById(rootMessageId);
        
        // Validar acceso
        if (!projectService.isUserMemberOfProject(userId, rootMessage.getProjectId())) {
            throw new ProjectMessageOperationNotAllowedException("No tienes acceso a este hilo de conversación");
        }
        
        // Obtener hilo completo
        List<ProjectMessage> threadMessages = projectMessageRepository.findMessageThread(rootMessageId);
        
        return threadMessages.stream()
            .map(projectMessageMapper::toResponseDto)
            .collect(Collectors.toList());
    }
    
    // Métodos privados de validación
    
    private void validateUserCanSendMessage(Long userId, Long projectId) {
        if (!userService.isUserActive(userId)) {
            throw new ProjectMessageOperationNotAllowedException("Usuario inactivo");
        }
        
        if (!projectService.isUserMemberOfProject(userId, projectId)) {
            throw new ProjectMessageOperationNotAllowedException("No eres miembro de este proyecto");
        }
        
        if (!projectService.isProjectActive(projectId)) {
            throw new ProjectMessageOperationNotAllowedException("El proyecto no está activo");
        }
    }
    
    private void validateRateLimiting(Long userId) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<ProjectMessage> recentMessages = projectMessageRepository.findRecentByUser(userId, oneHourAgo);
        
        if (!projectMessageDomainService.canUserSendMessage(userId, recentMessages)) {
            throw new ProjectMessageLimitExceededException("Has excedido el límite de mensajes por hora");
        }
    }
    
    private ProjectMessage findMessageById(Long messageId) {
        return projectMessageRepository.findById(messageId)
            .orElseThrow(() -> new ProjectMessageNotFoundException("Mensaje no encontrado: " + messageId));
    }
}
