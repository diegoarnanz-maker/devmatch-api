package com.devmatch.api.usernotification.application.dto;

import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para las respuestas de notificaciones.
 * Contiene todos los datos de una notificación para ser enviados al cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    
    private Long id;
    private Long userId;
    private String message;
    private NotificationType notificationType;
    private String notificationTypeDescription;
    private Long projectId;
    private Long reviewId;
    private String achievementCode;
    private boolean isRead;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 