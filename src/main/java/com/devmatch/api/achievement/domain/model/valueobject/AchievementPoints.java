package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa los puntos de un logro.
 * Encapsula las reglas de validación y formato de los puntos.
 */
public class AchievementPoints {
    private final int value;
    
    // Constantes para validación
    private static final int MIN_POINTS = 1;
    private static final int MAX_POINTS = 1000;
    
    public AchievementPoints(int value) {
        validatePoints(value);
        this.value = value;
    }
    
    private void validatePoints(int points) {
        if (points < MIN_POINTS) {
            throw new IllegalArgumentException(
                String.format("Los puntos del logro deben ser al menos %d", MIN_POINTS)
            );
        }
        
        if (points > MAX_POINTS) {
            throw new IllegalArgumentException(
                String.format("Los puntos del logro no pueden exceder %d", MAX_POINTS)
            );
        }
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isLow() {
        return value <= 10;
    }
    
    public boolean isMedium() {
        return value > 10 && value <= 50;
    }
    
    public boolean isHigh() {
        return value > 50 && value <= 100;
    }
    
    public boolean isEpic() {
        return value > 100;
    }
    
    public boolean isDivisibleBy(int divisor) {
        return value % divisor == 0;
    }
    
    public boolean isPrime() {
        if (value < 2) return false;
        for (int i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) return false;
        }
        return true;
    }
    
    public AchievementPoints add(AchievementPoints other) {
        return new AchievementPoints(this.value + other.value);
    }
    
    public AchievementPoints multiply(int factor) {
        return new AchievementPoints(this.value * factor);
    }
    
    public String getDisplayValue() {
        if (value >= 1000) {
            return String.format("%.1fk", value / 1000.0);
        } else if (value >= 100) {
            return String.format("%d", value);
        } else {
            return String.valueOf(value);
        }
    }
    
    public String getTier() {
        if (value <= 10) return "Bronce";
        if (value <= 25) return "Plata";
        if (value <= 50) return "Oro";
        if (value <= 100) return "Platino";
        if (value <= 250) return "Diamante";
        if (value <= 500) return "Mítico";
        return "Legendario";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementPoints that = (AchievementPoints) obj;
        return value == that.value;
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
