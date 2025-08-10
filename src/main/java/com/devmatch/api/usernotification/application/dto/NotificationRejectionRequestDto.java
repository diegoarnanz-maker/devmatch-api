package com.devmatch.api.usernotification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de notificación de rechazo de aplicación a proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRejectionRequestDto {

    @NotBlank(message = "El motivo del rechazo es obligatorio")
    @Size(max = 500, message = "El motivo del rechazo no puede exceder los 500 caracteres")
    private String reason;
}
