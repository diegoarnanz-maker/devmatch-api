package com.devmatch.api.usernotification.application.dto;

import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para las respuestas de notificaciones.
 * Contiene todos los datos de una notificación para ser enviados al cliente.
 * Campos basados en la tabla user_notifications del DDL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseDto {
    
    private Long id;
    private Long userId;
    private String message;
    private String notificationType;
    private Long projectId;
    private Long reviewId;
    private String achievementCode;
    private boolean isRead;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 