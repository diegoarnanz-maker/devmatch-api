package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el tipo de un logro.
 * Permite tipos dinámicos para que el admin pueda gestionar logros flexibly.
 * Encapsula las reglas de validación y formato del tipo.
 */
public class AchievementType {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String VALID_PATTERN = "^[A-Z_]+$";
    
    // Tipos predefinidos comunes (para referencia y validación)
    public static final String PROFILE = "PROFILE";
    public static final String PROJECT_CREATION = "PROJECT_CREATION";
    public static final String PROJECT_PARTICIPATION = "PROJECT_PARTICIPATION";
    public static final String PROJECT_APPLICATION = "PROJECT_APPLICATION";
    public static final String PROJECT_COMPLETION = "PROJECT_COMPLETION";
    public static final String REVIEW = "REVIEW";
    public static final String COLLABORATION = "COLLABORATION";
    public static final String LEADERSHIP = "LEADERSHIP";
    public static final String VETERAN = "VETERAN";
    public static final String GENERAL = "GENERAL";
    
    public AchievementType(String value) {
        validateType(value);
        this.value = value.trim().toUpperCase();
    }
    
    private void validateType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("El tipo del logro no puede ser nulo");
        }
        
        String trimmedType = type.trim();
        
        if (trimmedType.isEmpty()) {
            throw new IllegalArgumentException("El tipo del logro no puede estar vacío");
        }
        
        if (trimmedType.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El tipo debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmedType.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El tipo no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        if (!trimmedType.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                "El tipo solo puede contener letras mayúsculas y guiones bajos (ejemplo: MOBILE_DEVELOPMENT, AI_SPECIALIST)"
            );
        }
        
        // Validaciones específicas de negocio
        String upperType = trimmedType.toUpperCase();
        if (upperType.equals("TEST") || upperType.equals("EXAMPLE") || upperType.equals("DUMMY")) {
            throw new IllegalArgumentException("El tipo contiene palabras reservadas no permitidas");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return value.length();
    }
    
    public boolean isShort() {
        return value.length() <= 15;
    }
    
    public boolean isLong() {
        return value.length() >= 30;
    }
    
    public boolean containsUnderscore() {
        return value.contains("_");
    }
    
    public String getDisplayName() {
        return value.replace("_", " ");
    }
    
    public String getCapitalized() {
        if (value.isEmpty()) return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
    
    // Métodos de utilidad para tipos predefinidos
    public boolean isProfileRelated() {
        return PROFILE.equals(value);
    }
    
    public boolean isProjectRelated() {
        return PROJECT_CREATION.equals(value) || PROJECT_PARTICIPATION.equals(value) || 
               PROJECT_APPLICATION.equals(value) || PROJECT_COMPLETION.equals(value);
    }
    
    public boolean isReviewRelated() {
        return REVIEW.equals(value) || COLLABORATION.equals(value);
    }
    
    public boolean isLeadershipRelated() {
        return LEADERSHIP.equals(value);
    }
    
    public boolean isVeteranRelated() {
        return VETERAN.equals(value);
    }
    
    public boolean isBeginnerFriendly() {
        return PROFILE.equals(value) || GENERAL.equals(value) || PROJECT_APPLICATION.equals(value);
    }
    
    public boolean isAdvanced() {
        return VETERAN.equals(value) || LEADERSHIP.equals(value) || PROJECT_COMPLETION.equals(value);
    }
    
    public boolean isSocial() {
        return PROJECT_PARTICIPATION.equals(value) || COLLABORATION.equals(value) || LEADERSHIP.equals(value);
    }
    
    public boolean isTechnical() {
        return PROJECT_CREATION.equals(value) || PROJECT_COMPLETION.equals(value) || REVIEW.equals(value);
    }
    
    // Método para validar si un tipo es válido (útil para el admin)
    public static boolean isValidType(String type) {
        try {
            new AchievementType(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    // Método para obtener tipos sugeridos (útil para el admin)
    public static String[] getSuggestedTypes() {
        return new String[]{
            PROFILE, PROJECT_CREATION, PROJECT_PARTICIPATION, PROJECT_APPLICATION,
            PROJECT_COMPLETION, REVIEW, COLLABORATION, LEADERSHIP, VETERAN, GENERAL
        };
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementType that = (AchievementType) obj;
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
