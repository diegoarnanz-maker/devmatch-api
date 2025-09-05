package com.devmatch.api.projectmessage.application.mapper;

import com.devmatch.api.projectmessage.domain.model.MessageRead;
import com.devmatch.api.projectmessage.infrastructure.out.entity.MessageReadEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio y entidades JPA de MessageRead.
 */
@Component
public class MessageReadMapper {
    
    /**
     * Convierte una entidad de dominio a entidad JPA
     */
    public MessageReadEntity toEntity(MessageRead messageRead) {
        if (messageRead == null) {
            return null;
        }
        
        return MessageReadEntity.builder()
                .messageId(messageRead.getMessageId())
                .userId(messageRead.getUserId())
                .readAt(messageRead.getReadAt())
                .isNotified(messageRead.isNotified())
                .build();
    }
    
    /**
     * Convierte una entidad JPA a entidad de dominio
     */
    public MessageRead toDomain(MessageReadEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new MessageRead(
                entity.getMessageId(),
                entity.getUserId(),
                entity.getReadAt(),
                entity.getIsNotified()
        );
    }
}
