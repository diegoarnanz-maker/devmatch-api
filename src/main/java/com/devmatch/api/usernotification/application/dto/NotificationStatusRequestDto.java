package com.devmatch.api.usernotification.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la actualización del estado de notificaciones.
 * Contiene los campos que se pueden modificar en una notificación existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatusRequestDto {
    
    private Boolean isRead;
    private Boolean isActive;
    
    @Size(max = 500, message = "El mensaje no puede exceder los 500 caracteres")
    private String message;
    
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Verifica si el DTO tiene algún campo para actualizar.
     * 
     * @return true si hay al menos un campo no nulo para actualizar
     */
    public boolean hasUpdates() {
        return isRead != null || isActive != null || message != null;
    }
} 