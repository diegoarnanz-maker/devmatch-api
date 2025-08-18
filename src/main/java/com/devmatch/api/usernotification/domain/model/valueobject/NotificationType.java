package com.devmatch.api.usernotification.domain.model.valueobject;

public enum NotificationType {
    // Notificaciones de proyectos
    PROJECT_APPLICATION("PROJECT_APPLICATION", "Nueva aplicación a proyecto"),
    PROJECT_APPLICATION_RECEIVED("PROJECT_APPLICATION_RECEIVED", "Solicitud recibida por propietario"),
    PROJECT_APPLICATION_ACCEPTED("PROJECT_APPLICATION_ACCEPTED", "Aplicación aceptada"),
    PROJECT_APPLICATION_REJECTED("PROJECT_APPLICATION_REJECTED", "Aplicación rechazada"),
    PROJECT_APPLICATION_CANCELLED("PROJECT_APPLICATION_CANCELLED", "Aplicación cancelada"),
    PROJECT_APPLICATION_EXPIRED("PROJECT_APPLICATION_EXPIRED", "Aplicación expirada"),
    PROJECT_MEMBER_JOINED("PROJECT_MEMBER_JOINED", "Nuevo miembro en proyecto"),
    PROJECT_MEMBER_LEFT("PROJECT_MEMBER_LEFT", "Miembro abandonó proyecto"),
    
    // Notificaciones de reviews
    PROJECT_REVIEW_RECEIVED("PROJECT_REVIEW_RECEIVED", "Nueva review recibida"),
    PROJECT_REVIEW_RESPONSE("PROJECT_REVIEW_RESPONSE", "Respuesta a review"),
    
    // Notificaciones de logros
    ACHIEVEMENT_UNLOCKED("ACHIEVEMENT_UNLOCKED", "Logro desbloqueado"),
    
    // Notificaciones del sistema
    SYSTEM_MESSAGE("SYSTEM_MESSAGE", "Mensaje del sistema"),
    WELCOME_MESSAGE("WELCOME_MESSAGE", "Mensaje de bienvenida");

    private final String value;
    private final String description;

    NotificationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static NotificationType fromString(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de notificación no válido: " + value);
    }
} 