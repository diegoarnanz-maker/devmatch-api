package com.devmatch.api.projectmessage.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa el seguimiento de lectura de un mensaje por un usuario.
 */
@Getter
public class MessageRead {
    
    private final Long messageId;
    private final Long userId;
    private final LocalDateTime readAt;
    private final boolean isNotified;
    
    // Constructor para crear un nuevo registro de lectura
    public MessageRead(Long messageId, Long userId) {
        this.messageId = Objects.requireNonNull(messageId, "El ID del mensaje no puede ser nulo");
        this.userId = Objects.requireNonNull(userId, "El ID del usuario no puede ser nulo");
        this.readAt = LocalDateTime.now();
        this.isNotified = false;
    }
    
    // Constructor completo para reconstruir desde la base de datos
    public MessageRead(Long messageId, Long userId, LocalDateTime readAt, boolean isNotified) {
        this.messageId = messageId;
        this.userId = userId;
        this.readAt = readAt;
        this.isNotified = isNotified;
    }
    
    /**
     * Marca la notificación como enviada
     */
    public MessageRead markAsNotified() {
        return new MessageRead(this.messageId, this.userId, this.readAt, true);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRead that = (MessageRead) o;
        return Objects.equals(messageId, that.messageId) && 
               Objects.equals(userId, that.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageId, userId);
    }
}
