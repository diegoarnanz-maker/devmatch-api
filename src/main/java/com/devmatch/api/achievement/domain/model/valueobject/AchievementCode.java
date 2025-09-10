package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el código único de un logro.
 * 
 * <p>Encapsula las reglas de validación y formato del código.
 * Debe seguir el patrón ^[A-Z_]+$ y tener entre 3-50 caracteres.</p>
 * 
 * <p>Ejemplos válidos:</p>
 * <ul>
 *   <li>FIRST_PROJECT</li>
 *   <li>REVIEW_MASTER</li>
 *   <li>COLLABORATION_EXPERT</li>
 * </ul>
 * 
 * @see <a href="../../../../../docs/domain/achievement.md#achievementcode">Documentación completa</a>
 * @author DevMatch Team
 * @version 1.0
 * @since 2024
 */
public class AchievementCode {
    private final String value;
    
    // Constantes para validación
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String VALID_PATTERN = "^[A-Z_]+$";
    
    public AchievementCode(String value) {
        validateCode(value);
        this.value = value.trim().toUpperCase();
    }
    
    private void validateCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("El código del logro no puede ser nulo");
        }
        
        String trimmedCode = code.trim();
        
        if (trimmedCode.isEmpty()) {
            throw new IllegalArgumentException("El código del logro no puede estar vacío");
        }
        
        if (trimmedCode.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El código debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmedCode.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El código no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        if (!trimmedCode.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                "El código solo puede contener letras mayúsculas y guiones bajos (ejemplo: FIRST_PROJECT, REVIEW_MASTER)"
            );
        }
        
        // Validaciones específicas de negocio
        String upperCode = trimmedCode.toUpperCase();
        if (upperCode.equals("TEST") || upperCode.equals("EXAMPLE") || upperCode.equals("DUMMY")) {
            throw new IllegalArgumentException("El código contiene palabras reservadas no permitidas");
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementCode that = (AchievementCode) obj;
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
