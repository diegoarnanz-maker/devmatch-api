package com.devmatch.api.projectmessage.domain.model;

import com.devmatch.api.projectmessage.domain.model.valueobject.MessageContent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un mensaje en un proyecto.
 * Contiene la lógica de negocio para la gestión de mensajes entre miembros del proyecto.
 */
@Getter
public class ProjectMessage {
    
    private final Long id;
    private final Long projectId;
    private final Long senderId;
    private final MessageContent content;
    private final LocalDateTime sentAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean isDeleted;
    
    // Constructor para crear un nuevo mensaje
    public ProjectMessage(Long projectId, Long senderId, MessageContent content) {
        this.id = null; // Se asignará por la base de datos
        this.projectId = Objects.requireNonNull(projectId, "El ID del proyecto no puede ser nulo");
        this.senderId = Objects.requireNonNull(senderId, "El ID del remitente no puede ser nulo");
        this.content = Objects.requireNonNull(content, "El contenido del mensaje no puede ser nulo");
        this.sentAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
        this.isDeleted = false;
    }
    
    // Constructor completo para reconstruir desde la base de datos
    public ProjectMessage(Long id, Long projectId, Long senderId, MessageContent content, 
                         LocalDateTime sentAt, LocalDateTime createdAt, LocalDateTime updatedAt, 
                         boolean isDeleted) {
        this.id = id;
        this.projectId = projectId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = sentAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }
    
    /**
     * Edita el contenido del mensaje
     */
    public ProjectMessage editContent(MessageContent newContent) {
        if (this.isDeleted) {
            throw new IllegalStateException("No se puede editar un mensaje eliminado");
        }
        
        return new ProjectMessage(
            this.id, this.projectId, this.senderId, newContent,
            this.sentAt, this.createdAt, LocalDateTime.now(), this.isDeleted
        );
    }
    
    /**
     * Elimina el mensaje (soft delete)
     */
    public ProjectMessage delete() {
        if (this.isDeleted) {
            return this; // Ya está eliminado
        }
        
        return new ProjectMessage(
            this.id, this.projectId, this.senderId, this.content,
            this.sentAt, this.createdAt, LocalDateTime.now(), true
        );
    }
    
    /**
     * Verifica si el mensaje puede ser editado por un usuario
     */
    public boolean canBeEditedBy(Long userId) {
        return !this.isDeleted && this.senderId.equals(userId);
    }
    
    /**
     * Verifica si el mensaje puede ser eliminado por un usuario
     */
    public boolean canBeDeletedBy(Long userId) {
        return !this.isDeleted && this.senderId.equals(userId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMessage that = (ProjectMessage) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
