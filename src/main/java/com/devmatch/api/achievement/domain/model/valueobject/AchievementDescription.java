package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa la descripción de un logro.
 * Valida longitud (10-500 caracteres) y formato con caracteres permitidos.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementDescription {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 500;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9\\s\\-_\\.\\ñ\\á\\é\\í\\ó\\ú\\ü\\Á\\É\\Í\\Ó\\Ú\\Ü\\,\\;\\!\\.\\?\\:\\(\\)\\¡\\¿]+$";
    
    public AchievementDescription(String value) {
        validateDescription(value);
        this.value = value.trim();
    }
    
    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("La descripción del logro no puede ser nula");
        }
        
        String trimmedDescription = description.trim();
        
        if (trimmedDescription.isEmpty()) {
            throw new IllegalArgumentException("La descripción del logro no puede estar vacía");
        }
        
        if (trimmedDescription.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("La descripción debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmedDescription.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("La descripción no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        if (!trimmedDescription.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                "La descripción contiene caracteres no permitidos. Solo se permiten letras, números, espacios, guiones, puntos, acentos y signos de puntuación básicos"
            );
        }
        
        // Validaciones específicas de negocio para logros
        String lowerDescription = trimmedDescription.toLowerCase();
        if (lowerDescription.contains("spam") || 
            lowerDescription.contains("test") ||
            lowerDescription.contains("prueba") ||
            lowerDescription.contains("lorem ipsum") ||
            lowerDescription.contains("descripción de prueba")) {
            throw new IllegalArgumentException("La descripción contiene palabras no permitidas");
        }
        
        // Validar que no sea solo caracteres repetidos
        if (trimmedDescription.matches("^[\\s\\-_\\.]+$")) {
            throw new IllegalArgumentException("La descripción no puede contener solo caracteres especiales");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return value.length();
    }
    
    public boolean isShort() {
        return value.length() <= 50;
    }
    
    public boolean isLong() {
        return value.length() >= 200;
    }
    
    public boolean containsPunctuation() {
        return value.matches(".*[\\,\\;\\!\\.\\?\\:\\(\\)].*");
    }
    
    public int getWordCount() {
        return value.split("\\s+").length;
    }
    
    public boolean isDetailed() {
        return getWordCount() >= 10;
    }
    
    public String getFirstSentence() {
        int endIndex = value.indexOf('.');
        return endIndex > 0 ? value.substring(0, endIndex + 1) : value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementDescription that = (AchievementDescription) obj;
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
