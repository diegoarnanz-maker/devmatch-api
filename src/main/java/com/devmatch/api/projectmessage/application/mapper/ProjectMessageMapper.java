package com.devmatch.api.projectmessage.application.mapper;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageUpdateRequestDto;
import com.devmatch.api.projectmessage.domain.model.ProjectMessage;
import com.devmatch.api.projectmessage.domain.model.valueobject.MessageContent;
import com.devmatch.api.projectmessage.domain.model.valueobject.MessageType;
import com.devmatch.api.projectmessage.domain.model.valueobject.MessageStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de aplicación.
 */
@Component
public class ProjectMessageMapper {
    
    /**
     * Convierte un DTO de request a una entidad de dominio
     */
    public ProjectMessage toDomain(ProjectMessageRequestDto requestDto, Long senderId) {
        MessageContent content = new MessageContent(requestDto.getContent());
        MessageType type = MessageType.fromValue(requestDto.getMessageType());
        
        return new ProjectMessage(
            requestDto.getProjectId(),
            senderId,
            content,
            type,
            requestDto.getReplyToMessageId()
        );
    }
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta
     */
    public ProjectMessageResponseDto toResponseDto(ProjectMessage message) {
        return ProjectMessageResponseDto.builder()
            .id(message.getId())
            .projectId(message.getProjectId())
            .senderId(message.getSenderId())
            .content(message.getContent().getValue())
            .messageType(messageTypeToString(message.getType()))
            .replyToMessageId(message.getReplyToMessageId())
            .sentAt(message.getSentAt())
            .createdAt(message.getCreatedAt())
            .updatedAt(message.getUpdatedAt())
            .isDeleted(message.isDeleted())
            .build();
    }
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta con información del remitente
     */
    public ProjectMessageResponseDto toResponseDtoWithSender(ProjectMessage message, String username, String profileImageUrl, String role) {
        ProjectMessageResponseDto.SenderInfo senderInfo = ProjectMessageResponseDto.SenderInfo.builder()
            .userId(message.getSenderId())
            .username(username)
            .profileImageUrl(profileImageUrl)
            .role(role)
            .build();
        
        return ProjectMessageResponseDto.builder()
            .id(message.getId())
            .projectId(message.getProjectId())
            .senderId(message.getSenderId())
            .senderUsername(username)
            .content(message.getContent().getValue())
            .messageType(messageTypeToString(message.getType()))
            .replyToMessageId(message.getReplyToMessageId())
            .sentAt(message.getSentAt())
            .createdAt(message.getCreatedAt())
            .updatedAt(message.getUpdatedAt())
            .isDeleted(message.isDeleted())
            .sender(senderInfo)
            .build();
    }
    
    /**
     * Convierte una lista de entidades de dominio a una lista de DTOs de respuesta
     */
    public List<ProjectMessageResponseDto> toResponseDtoList(List<ProjectMessage> messages) {
        return messages.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Crea un nuevo MessageContent desde un DTO de actualización
     */
    public MessageContent toMessageContent(ProjectMessageUpdateRequestDto updateDto) {
        return new MessageContent(updateDto.getContent());
    }
    
    /**
     * Convierte un MessageType a String
     */
    public String messageTypeToString(MessageType type) {
        return type != null ? type.getValue() : null;
    }
    
    /**
     * Convierte un MessageStatus a String
     */
    public String messageStatusToString(MessageStatus status) {
        return status != null ? status.getValue() : null;
    }
}
