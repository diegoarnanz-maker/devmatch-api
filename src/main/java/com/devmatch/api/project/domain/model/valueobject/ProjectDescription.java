package com.devmatch.api.project.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa la descripción de un proyecto.
 * Encapsula las reglas de validación y formato de la descripción.
 */
public class ProjectDescription {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 20;
    private static final int MAX_LENGTH = 2000;
    private static final int MIN_WORDS = 5;
    private static final int MAX_WORDS = 500;
    
    public ProjectDescription(String value) {
        validateDescription(value);
        this.value = value.trim();
    }
    
    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("La descripción del proyecto no puede ser nula");
        }
        
        String trimmedDescription = description.trim();
        
        if (trimmedDescription.isEmpty()) {
            throw new IllegalArgumentException("La descripción del proyecto no puede estar vacía");
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
        
        // Validar número de palabras
        int wordCount = trimmedDescription.split("\\s+").length;
        if (wordCount < MIN_WORDS) {
            throw new IllegalArgumentException(
                String.format("La descripción debe tener al menos %d palabras", MIN_WORDS)
            );
        }
        
        if (wordCount > MAX_WORDS) {
            throw new IllegalArgumentException(
                String.format("La descripción no puede exceder %d palabras", MAX_WORDS)
            );
        }
        
        // Validar que no sea solo caracteres repetidos
        if (trimmedDescription.matches("^[\\s\\W]+$")) {
            throw new IllegalArgumentException("La descripción no puede contener solo espacios o caracteres especiales");
        }
        
        // Validar contenido inapropiado
        String lowerDescription = trimmedDescription.toLowerCase();
        if (lowerDescription.contains("spam") || 
            lowerDescription.contains("test") ||
            lowerDescription.contains("prueba") ||
            lowerDescription.contains("lorem ipsum")) {
            throw new IllegalArgumentException("La descripción contiene contenido no permitido");
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
        ProjectDescription that = (ProjectDescription) obj;
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