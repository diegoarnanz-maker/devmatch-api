package com.devmatch.api.project.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el mensaje de motivación en una aplicación a un proyecto.
 * Encapsula las reglas de validación para el mensaje de motivación.
 */
public class MotivationMessage {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 1000;
    private static final int MIN_WORDS = 5;
    private static final int MAX_WORDS = 200;
    
    public MotivationMessage(String value) {
        validateMessage(value);
        this.value = value.trim();
    }
    
    private void validateMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("El mensaje de motivación no puede ser nulo");
        }
        
        String trimmedMessage = message.trim();
        
        if (trimmedMessage.isEmpty()) {
            throw new IllegalArgumentException("El mensaje de motivación no puede estar vacío");
        }
        
        if (trimmedMessage.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El mensaje de motivación debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmedMessage.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El mensaje de motivación no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        // Validar número de palabras
        int wordCount = trimmedMessage.split("\\s+").length;
        if (wordCount < MIN_WORDS) {
            throw new IllegalArgumentException(
                String.format("El mensaje de motivación debe tener al menos %d palabras", MIN_WORDS)
            );
        }
        
        if (wordCount > MAX_WORDS) {
            throw new IllegalArgumentException(
                String.format("El mensaje de motivación no puede exceder %d palabras", MAX_WORDS)
            );
        }
        
        // Validar contenido inapropiado
        String lowerMessage = trimmedMessage.toLowerCase();
        if (lowerMessage.contains("spam") || 
            lowerMessage.contains("test") ||
            lowerMessage.contains("prueba") ||
            lowerMessage.contains("lorem ipsum")) {
            throw new IllegalArgumentException("El mensaje de motivación contiene contenido no permitido");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return value.length();
    }
    
    public int getWordCount() {
        return value.split("\\s+").length;
    }
    
    public boolean isShort() {
        return getWordCount() <= 20;
    }
    
    public boolean isLong() {
        return getWordCount() >= 100;
    }
    
    public boolean containsKeyword(String keyword) {
        return value.toLowerCase().contains(keyword.toLowerCase());
    }
    
    public String getSummary(int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MotivationMessage that = (MotivationMessage) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
} 