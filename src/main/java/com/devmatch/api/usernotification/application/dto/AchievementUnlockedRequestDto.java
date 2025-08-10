package com.devmatch.api.usernotification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de notificación de logro desbloqueado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementUnlockedRequestDto {

    @NotBlank(message = "El nombre del logro es obligatorio")
    @Size(max = 100, message = "El nombre del logro no puede exceder los 100 caracteres")
    private String achievementName;
}
