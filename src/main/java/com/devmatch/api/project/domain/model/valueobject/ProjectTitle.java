package com.devmatch.api.project.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el título de un proyecto.
 * Encapsula las reglas de validación y formato del título.
 */
public class ProjectTitle {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 100;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9\\s\\-_\\.\\ñ\\á\\é\\í\\ó\\ú\\ü\\Á\\É\\Í\\Ó\\Ú\\Ü]+$";
    
    public ProjectTitle(String value) {
        validateTitle(value);
        this.value = value.trim();
    }
    
    private void validateTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("El título del proyecto no puede ser nulo");
        }
        
        String trimmedTitle = title.trim();
        
        if (trimmedTitle.isEmpty()) {
            throw new IllegalArgumentException("El título del proyecto no puede estar vacío");
        }
        
        if (trimmedTitle.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El título debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmedTitle.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El título no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        if (!trimmedTitle.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                "El título contiene caracteres no permitidos. Solo se permiten letras, números, espacios, guiones, puntos y acentos"
            );
        }
        
        // Validaciones específicas de negocio - solo rechazar títulos que sean exactamente palabras de prueba
        String lowerTitle = trimmedTitle.toLowerCase();
        if (lowerTitle.equals("spam") || 
            lowerTitle.equals("test") ||
            lowerTitle.equals("prueba") ||
            lowerTitle.equals("lorem ipsum")) {
            throw new IllegalArgumentException("El título contiene palabras no permitidas");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return value.length();
    }
    
    public boolean isShort() {
        return value.length() <= 20;
    }
    
    public boolean isLong() {
        return value.length() >= 50;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProjectTitle that = (ProjectTitle) obj;
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