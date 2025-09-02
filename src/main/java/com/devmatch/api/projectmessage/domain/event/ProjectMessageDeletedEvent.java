package com.devmatch.api.projectmessage.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Evento de dominio que se dispara cuando se elimina un mensaje del proyecto.
 */
@Getter
public class ProjectMessageDeletedEvent extends ApplicationEvent {
    
    private final Long messageId;
    private final Long projectId;
    private final Long deleterId;
    private final LocalDateTime deletedAt;
    
    public ProjectMessageDeletedEvent(Object source, Long messageId, Long projectId, Long deleterId) {
        super(source);
        this.messageId = messageId;
        this.projectId = projectId;
        this.deleterId = deleterId;
        this.deletedAt = LocalDateTime.now();
    }
}
