package com.devmatch.api.usernotification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de notificación de review recibida.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReviewReceivedRequestDto {

    @NotBlank(message = "El nombre del revisor es obligatorio")
    @Size(max = 100, message = "El nombre del revisor no puede exceder los 100 caracteres")
    private String reviewerName;
}
