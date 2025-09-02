package com.devmatch.api.projectmessage.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Evento de dominio que se dispara cuando se lee un mensaje del proyecto.
 */
@Getter
public class ProjectMessageReadEvent extends ApplicationEvent {
    
    private final Long messageId;
    private final Long projectId;
    private final Long readerId;
    private final LocalDateTime readAt;
    
    public ProjectMessageReadEvent(Object source, Long messageId, Long projectId, Long readerId) {
        super(source);
        this.messageId = messageId;
        this.projectId = projectId;
        this.readerId = readerId;
        this.readAt = LocalDateTime.now();
    }
}
