package com.devmatch.api.project.domain.model.valueobject;

/**
 * Estados posibles de una aplicación a proyecto.
 *
 * @author diegoarnanz-maker
 * @since 2025
 */
public enum ApplicationStatus {
    PENDING("PENDING", "Pendiente de revisión"),
    ACCEPTED("ACCEPTED", "Aceptada"),
    REJECTED("REJECTED", "Rechazada"),
    EXPIRED("EXPIRED", "Expirada por inactividad");

    private final String value;
    private final String description;

    ApplicationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ApplicationStatus fromValue(String value) {
        for (ApplicationStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de aplicación no válido: " + value);
    }
} 