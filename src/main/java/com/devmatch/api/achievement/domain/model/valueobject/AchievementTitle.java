package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el título de un logro.
 * Valida formato y longitud (5-100 caracteres).
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class AchievementTitle {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 100;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9\\s\\-_\\.\\ñ\\á\\é\\í\\ó\\ú\\ü\\Á\\É\\Í\\Ó\\Ú\\Ü\\¡\\¿\\!\\?]+$";
    
    public AchievementTitle(String value) {
        validateTitle(value);
        this.value = value.trim();
    }
    
    private void validateTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("El título del logro no puede ser nulo");
        }
        
        String trimmedTitle = title.trim();
        
        if (trimmedTitle.isEmpty()) {
            throw new IllegalArgumentException("El título del logro no puede estar vacío");
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
        
        // Validaciones específicas de negocio para logros
        String lowerTitle = trimmedTitle.toLowerCase();
        if (lowerTitle.equals("spam") || 
            lowerTitle.equals("test") ||
            lowerTitle.equals("prueba") ||
            lowerTitle.equals("lorem ipsum") ||
            lowerTitle.equals("logro de prueba")) {
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
    
    public boolean containsEmoji() {
        return value.matches(".*[\\p{So}].*");
    }
    
    public String getCapitalized() {
        if (value.isEmpty()) return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementTitle that = (AchievementTitle) obj;
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
