package com.devmatch.api.usernotification.application.dto;

import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la creación de notificaciones.
 * Contiene todos los datos necesarios para crear una nueva notificación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
    
    @NotNull(message = "El mensaje de la notificación es obligatorio")
    @Size(min = 1, max = 500, message = "El mensaje debe tener entre 1 y 500 caracteres")
    private String message;
    
    @NotNull(message = "El tipo de notificación es obligatorio")
    private NotificationType notificationType;
    
    private Long projectId;
    private Long reviewId;
    private String achievementCode;
    private boolean isRead = false;
    private boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
} 