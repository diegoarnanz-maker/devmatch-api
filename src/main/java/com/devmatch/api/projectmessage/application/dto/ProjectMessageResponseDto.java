package com.devmatch.api.projectmessage.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de un mensaje del proyecto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMessageResponseDto {
    
    private Long id;
    private Long projectId;
    private Long senderId;
    private String senderUsername; // Nombre del usuario que envió el mensaje
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    
    // Información adicional para respuestas
    private ProjectMessageResponseDto replyToMessage; // Mensaje al que responde (si aplica)
    
    // Información del remitente
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderInfo {
        private Long userId;
        private String username;
        private String profileImageUrl;
        private String role; // Rol en el proyecto
    }
    
    private SenderInfo sender;
}
