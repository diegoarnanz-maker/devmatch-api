package com.devmatch.api.project.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el tamaño del equipo de un proyecto.
 * Encapsula las reglas de validación para el número máximo de miembros.
 */
public class TeamSize {
    private final int value;
    
    // Constantes para validación
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 20;
    private static final int RECOMMENDED_MIN_SIZE = 2;
    private static final int RECOMMENDED_MAX_SIZE = 8;
    
    public TeamSize(int value) {
        validateTeamSize(value);
        this.value = value;
    }
    
    private void validateTeamSize(int size) {
        if (size < MIN_SIZE) {
            throw new IllegalArgumentException(
                String.format("El tamaño del equipo debe ser al menos %d", MIN_SIZE)
            );
        }
        
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException(
                String.format("El tamaño del equipo no puede exceder %d", MAX_SIZE)
            );
        }
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isSmall() {
        return value <= 3;
    }
    
    public boolean isMedium() {
        return value > 3 && value <= 6;
    }
    
    public boolean isLarge() {
        return value > 6;
    }
    
    public boolean isRecommended() {
        return value >= RECOMMENDED_MIN_SIZE && value <= RECOMMENDED_MAX_SIZE;
    }
    
    public boolean isTooSmall() {
        return value < RECOMMENDED_MIN_SIZE;
    }
    
    public boolean isTooLarge() {
        return value > RECOMMENDED_MAX_SIZE;
    }
    
    public String getSizeCategory() {
        if (isSmall()) {
            return "Pequeño";
        } else if (isMedium()) {
            return "Mediano";
        } else {
            return "Grande";
        }
    }
    
    public String getRecommendation() {
        if (isTooSmall()) {
            return "Considera aumentar el equipo para mejor colaboración";
        } else if (isTooLarge()) {
            return "Equipos muy grandes pueden ser difíciles de coordinar";
        } else {
            return "Tamaño de equipo ideal para la mayoría de proyectos";
        }
    }
    
    public boolean canAcceptMoreMembers(int currentMembers) {
        return currentMembers < value;
    }
    
    public int getAvailableSlots(int currentMembers) {
        return Math.max(0, value - currentMembers);
    }
    
    public boolean isFull(int currentMembers) {
        return currentMembers >= value;
    }
    
    public double getUtilizationPercentage(int currentMembers) {
        if (value == 0) return 0.0;
        return (double) currentMembers / value * 100;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TeamSize teamSize = (TeamSize) obj;
        return value == teamSize.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
} 