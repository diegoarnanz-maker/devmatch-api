package com.devmatch.api.projectmessage.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa una mención de usuario en un mensaje.
 */
@Getter
public class MessageMention {
    
    private final Long id;
    private final Long messageId;
    private final Long mentionedUserId;
    private final LocalDateTime createdAt;
    
    // Constructor para crear una nueva mención
    public MessageMention(Long messageId, Long mentionedUserId) {
        this.id = null; // Se asignará por la base de datos
        this.messageId = Objects.requireNonNull(messageId, "El ID del mensaje no puede ser nulo");
        this.mentionedUserId = Objects.requireNonNull(mentionedUserId, "El ID del usuario mencionado no puede ser nulo");
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor completo para reconstruir desde la base de datos
    public MessageMention(Long id, Long messageId, Long mentionedUserId, LocalDateTime createdAt) {
        this.id = id;
        this.messageId = messageId;
        this.mentionedUserId = mentionedUserId;
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageMention that = (MessageMention) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
