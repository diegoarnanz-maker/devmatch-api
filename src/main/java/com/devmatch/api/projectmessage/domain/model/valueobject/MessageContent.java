package com.devmatch.api.projectmessage.domain.model.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value Object para el contenido de un mensaje del proyecto.
 * Representa el texto del mensaje con validaciones de negocio.
 */
@Getter
@EqualsAndHashCode
public class MessageContent {
    
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 2000;
    
    private final String value;
    
    public MessageContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("El contenido del mensaje no puede ser nulo");
        }
        
        String trimmedContent = content.trim();
        
        if (trimmedContent.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }
        
        if (trimmedContent.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El contenido del mensaje no puede exceder " + MAX_LENGTH + " caracteres");
        }
        
        // Validaciones específicas de negocio para mensajes
        String lowerContent = trimmedContent.toLowerCase();
        if (lowerContent.contains("spam") || 
            lowerContent.contains("test") ||
            lowerContent.contains("prueba") ||
            lowerContent.contains("lorem ipsum") ||
            lowerContent.contains("mensaje de prueba")) {
            throw new IllegalArgumentException("El contenido del mensaje contiene palabras no permitidas");
        }
        
        this.value = trimmedContent;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
