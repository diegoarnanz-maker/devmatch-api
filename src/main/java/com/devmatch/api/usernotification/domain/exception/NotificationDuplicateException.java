package com.devmatch.api.usernotification.domain.exception;

import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;

/**
 * Excepción lanzada cuando se intenta crear una notificación duplicada.
 */
public class NotificationDuplicateException extends RuntimeException {

    public NotificationDuplicateException(String message) {
        super(message);
    }

    public NotificationDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationDuplicateException(Long userId, NotificationType notificationType, Long relatedId) {
        super(String.format("Ya existe una notificación reciente del tipo '%s' para el usuario %d " +
                "relacionada con el ID %d", notificationType.getDescription(), userId, relatedId));
    }

    public NotificationDuplicateException(Long userId, NotificationType notificationType) {
        super(String.format("Ya existe una notificación reciente del tipo '%s' para el usuario %d", 
                notificationType.getDescription(), userId));
    }

    public NotificationDuplicateException(Long userId, String notificationType, int timeWindowMinutes) {
        super(String.format("Existe una notificación duplicada del tipo '%s' para el usuario %d " +
                "en los últimos %d minutos", notificationType, userId, timeWindowMinutes));
    }
} 