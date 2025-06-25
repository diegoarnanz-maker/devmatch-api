package com.devmatch.api.project.domain.model.valueobject;

/**
 * Value object que representa el estado de una aplicación a un proyecto.
 * Basado en la tabla project_applications del DDL.
 */
public enum ApplicationStatus {
    PENDING("PENDING", "Pendiente de revisión"),
    ACCEPTED("ACCEPTED", "Aceptada"),
    REJECTED("REJECTED", "Rechazada");

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