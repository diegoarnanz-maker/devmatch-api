package com.devmatch.api.usernotification.domain.exception;

/**
 * Excepción lanzada cuando se excede el límite de notificaciones permitidas para un usuario.
 */
public class NotificationLimitExceededException extends RuntimeException {

    public NotificationLimitExceededException(String message) {
        super(message);
    }

    public NotificationLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationLimitExceededException(Long userId, int currentCount, int maxLimit) {
        super(String.format("Se ha excedido el límite de notificaciones para el usuario %d. " +
                "Notificaciones actuales: %d, Límite máximo: %d", userId, currentCount, maxLimit));
    }

    public NotificationLimitExceededException(Long userId, int maxLimit) {
        super(String.format("El usuario %d ya tiene el máximo de %d notificaciones permitidas", userId, maxLimit));
    }
} 