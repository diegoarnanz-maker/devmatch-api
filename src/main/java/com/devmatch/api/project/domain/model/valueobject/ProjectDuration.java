package com.devmatch.api.project.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa la duración estimada de un proyecto en semanas.
 * Encapsula las reglas de validación para la duración del proyecto.
 */
public class ProjectDuration {
    private final int weeks;
    
    // Constantes para validación
    private static final int MIN_WEEKS = 1;
    private static final int MAX_WEEKS = 104; // 2 años
    private static final int RECOMMENDED_MIN_WEEKS = 2;
    private static final int RECOMMENDED_MAX_WEEKS = 52; // 1 año
    
    public ProjectDuration(int weeks) {
        validateDuration(weeks);
        this.weeks = weeks;
    }
    
    private void validateDuration(int weeks) {
        if (weeks < MIN_WEEKS) {
            throw new IllegalArgumentException(
                String.format("La duración debe ser al menos %d semana", MIN_WEEKS)
            );
        }
        
        if (weeks > MAX_WEEKS) {
            throw new IllegalArgumentException(
                String.format("La duración no puede exceder %d semanas (2 años)", MAX_WEEKS)
            );
        }
    }
    
    public int getWeeks() {
        return weeks;
    }
    
    public int getMonths() {
        return (int) Math.ceil(weeks / 4.33); // Promedio de semanas por mes
    }
    
    public int getYears() {
        return (int) Math.ceil(weeks / 52.0); // Promedio de semanas por año
    }
    
    public boolean isShort() {
        return weeks <= 4;
    }
    
    public boolean isMedium() {
        return weeks > 4 && weeks <= 12;
    }
    
    public boolean isLong() {
        return weeks > 12;
    }
    
    public boolean isRecommended() {
        return weeks >= RECOMMENDED_MIN_WEEKS && weeks <= RECOMMENDED_MAX_WEEKS;
    }
    
    public boolean isTooShort() {
        return weeks < RECOMMENDED_MIN_WEEKS;
    }
    
    public boolean isTooLong() {
        return weeks > RECOMMENDED_MAX_WEEKS;
    }
    
    public String getDurationCategory() {
        if (isShort()) {
            return "Corto plazo";
        } else if (isMedium()) {
            return "Mediano plazo";
        } else {
            return "Largo plazo";
        }
    }
    
    public String getRecommendation() {
        if (isTooShort()) {
            return "Considera aumentar la duración para un desarrollo más completo";
        } else if (isTooLong()) {
            return "Proyectos muy largos pueden ser difíciles de mantener";
        } else {
            return "Duración ideal para la mayoría de proyectos";
        }
    }
    
    public String getFormattedDuration() {
        if (weeks == 1) {
            return "1 semana";
        } else if (weeks < 52) {
            return weeks + " semanas";
        } else {
            int years = weeks / 52;
            int remainingWeeks = weeks % 52;
            if (remainingWeeks == 0) {
                return years + (years == 1 ? " año" : " años");
            } else {
                return years + (years == 1 ? " año" : " años") + " y " + remainingWeeks + " semanas";
            }
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProjectDuration that = (ProjectDuration) obj;
        return weeks == that.weeks;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(weeks);
    }
    
    @Override
    public String toString() {
        return String.valueOf(weeks);
    }
} 