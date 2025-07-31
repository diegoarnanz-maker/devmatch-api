package com.devmatch.api.usernotification.domain.exception;

/**
 * Excepción lanzada cuando el estado de la notificación no permite realizar la operación solicitada.
 */
public class NotificationInvalidStateException extends RuntimeException {

    public NotificationInvalidStateException(String message) {
        super(message);
    }

    public NotificationInvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationInvalidStateException(Long notificationId, String currentState, String requiredState) {
        super(String.format("La notificación %d tiene un estado inválido para esta operación. " +
                "Estado actual: %s, Estado requerido: %s", notificationId, currentState, requiredState));
    }

    public NotificationInvalidStateException(Long notificationId, String operation, String reason, boolean isOperation) {
        super(String.format("No se puede %s la notificación %d: %s", operation, notificationId, reason));
    }

    public NotificationInvalidStateException(Long notificationId, boolean isRead, boolean isActive, boolean isDeleted) {
        super(String.format("Estado inválido de la notificación %d: leída=%s, activa=%s, eliminada=%s", 
                notificationId, isRead, isActive, isDeleted));
    }

    public static NotificationInvalidStateException alreadyRead(Long notificationId) {
        return new NotificationInvalidStateException(notificationId, "marcar como leída", 
                "la notificación ya está marcada como leída", true);
    }

    public static NotificationInvalidStateException alreadyDeleted(Long notificationId) {
        return new NotificationInvalidStateException(notificationId, "eliminar", 
                "la notificación ya está eliminada", true);
    }

    public static NotificationInvalidStateException notActive(Long notificationId) {
        return new NotificationInvalidStateException(notificationId, "operar", 
                "la notificación no está activa", true);
    }
} 