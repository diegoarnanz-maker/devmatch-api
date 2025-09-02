package com.devmatch.api.projectmessage.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Evento de dominio que se dispara cuando se edita un mensaje del proyecto.
 */
@Getter
public class ProjectMessageEditedEvent extends ApplicationEvent {
    
    private final Long messageId;
    private final Long projectId;
    private final Long editorId;
    private final String oldContent;
    private final String newContent;
    private final LocalDateTime editedAt;
    
    public ProjectMessageEditedEvent(Object source, Long messageId, Long projectId, Long editorId, 
                                   String oldContent, String newContent) {
        super(source);
        this.messageId = messageId;
        this.projectId = projectId;
        this.editorId = editorId;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.editedAt = LocalDateTime.now();
    }
}
