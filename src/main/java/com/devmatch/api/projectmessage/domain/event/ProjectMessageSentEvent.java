package com.devmatch.api.projectmessage.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Evento de dominio que se dispara cuando se envía un mensaje en un proyecto.
 */
@Getter
public class ProjectMessageSentEvent extends ApplicationEvent {
    
    private final Long messageId;
    private final Long projectId;
    private final Long senderId;
    private final String messageType;
    private final LocalDateTime sentAt;
    
    public ProjectMessageSentEvent(Object source, Long messageId, Long projectId, Long senderId, String messageType) {
        super(source);
        this.messageId = messageId;
        this.projectId = projectId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.sentAt = LocalDateTime.now();
    }
}
