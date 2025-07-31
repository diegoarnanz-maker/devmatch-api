package com.devmatch.api.usernotification.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una notificación específica.
 */
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationNotFoundException(Long notificationId) {
        super(String.format("No se encontró la notificación con ID: %d", notificationId));
    }

    public NotificationNotFoundException(Long notificationId, Long userId) {
        super(String.format("No se encontró la notificación con ID %d para el usuario %d", notificationId, userId));
    }

    public NotificationNotFoundException(String criteria, String value) {
        super(String.format("No se encontró ninguna notificación con %s: %s", criteria, value));
    }

    public static NotificationNotFoundException byId(Long notificationId) {
        return new NotificationNotFoundException(notificationId);
    }

    public static NotificationNotFoundException byIdAndUser(Long notificationId, Long userId) {
        return new NotificationNotFoundException(notificationId, userId);
    }

    public static NotificationNotFoundException byType(String notificationType) {
        return new NotificationNotFoundException("tipo", notificationType);
    }
} 