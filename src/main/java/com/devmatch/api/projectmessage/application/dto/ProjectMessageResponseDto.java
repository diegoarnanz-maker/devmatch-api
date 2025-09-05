package com.devmatch.api.projectmessage.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String messageType; // Tipo de mensaje (TEXT, ANNOUNCEMENT, etc.)
    private Long replyToMessageId; // ID del mensaje al que responde (si aplica)
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isRead; // Indica si el mensaje ha sido leído por el usuario actual
    private LocalDateTime readAt; // Fecha y hora en que fue leído
    
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
    
    /**
     * Getter personalizado para evitar duplicación en JSON
     */
    @JsonProperty("isRead")
    public boolean isRead() {
        return isRead;
    }
    
    /**
     * Getter para el campo read (ignorado en JSON)
     */
    @JsonIgnore
    public boolean getRead() {
        return isRead;
    }
}
