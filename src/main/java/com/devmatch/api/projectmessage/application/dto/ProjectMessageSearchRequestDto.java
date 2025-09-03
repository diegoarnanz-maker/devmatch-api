package com.devmatch.api.projectmessage.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para búsqueda y filtrado de mensajes del proyecto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMessageSearchRequestDto {
    
    private Long projectId;
    private Long senderId;
    private String messageType; // TEXT, ANNOUNCEMENT, TASK_UPDATE, etc.
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String contentSearch; // Búsqueda por contenido
    private Boolean includeDeleted;
    private Boolean includeReplies;
    private Long replyToMessageId;
    
    // Filtros de paginación
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection; // ASC, DESC
}
