package com.devmatch.api.usernotification.domain.exception;

/**
 * Excepción genérica lanzada cuando una operación no está permitida en una notificación.
 * Se recomienda usar excepciones más específicas cuando sea posible.
 */
public class NotificationOperationNotAllowedException extends RuntimeException {

    public NotificationOperationNotAllowedException(String message) {
        super(message);
    }

    public NotificationOperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationOperationNotAllowedException(Long notificationId, String operation) {
        super(String.format("La operación '%s' no está permitida en la notificación %d", operation, notificationId));
    }

    public NotificationOperationNotAllowedException(Long notificationId, String operation, String reason) {
        super(String.format("La operación '%s' no está permitida en la notificación %d: %s", 
                operation, notificationId, reason));
    }

    public NotificationOperationNotAllowedException(String operation, String reason) {
        super(String.format("La operación '%s' no está permitida: %s", operation, reason));
    }

    public static NotificationOperationNotAllowedException forOperation(Long notificationId, String operation) {
        return new NotificationOperationNotAllowedException(notificationId, operation);
    }

    public static NotificationOperationNotAllowedException forOperation(Long notificationId, String operation, String reason) {
        return new NotificationOperationNotAllowedException(notificationId, operation, reason);
    }

    public static NotificationOperationNotAllowedException forOperation(String operation, String reason) {
        return new NotificationOperationNotAllowedException(operation, reason);
    }
} 