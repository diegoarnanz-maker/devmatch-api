package com.devmatch.api.usernotification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de notificación de nuevo miembro unido al proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberJoinedRequestDto {

    @NotBlank(message = "El nombre del miembro es obligatorio")
    @Size(max = 100, message = "El nombre del miembro no puede exceder los 100 caracteres")
    private String memberName;
}
