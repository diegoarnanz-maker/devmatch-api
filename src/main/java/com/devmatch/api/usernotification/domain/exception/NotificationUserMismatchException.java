package com.devmatch.api.usernotification.domain.exception;

/**
 * Excepción lanzada cuando un usuario intenta acceder a una notificación que no le pertenece.
 */
public class NotificationUserMismatchException extends RuntimeException {

    public NotificationUserMismatchException(String message) {
        super(message);
    }

    public NotificationUserMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationUserMismatchException(Long notificationId, Long requestedUserId, Long actualUserId) {
        super(String.format("El usuario %d no puede acceder a la notificación %d que pertenece al usuario %d", 
                requestedUserId, notificationId, actualUserId));
    }

    public NotificationUserMismatchException(Long requestedUserId, Long actualUserId) {
        super(String.format("Acceso denegado: el usuario %d intentó acceder a una notificación del usuario %d", 
                requestedUserId, actualUserId));
    }

    public NotificationUserMismatchException(String operation, Long notificationId, Long requestedUserId, Long actualUserId) {
        super(String.format("No se puede %s la notificación %d: el usuario %d no es propietario de la notificación " +
                "que pertenece al usuario %d", operation, notificationId, requestedUserId, actualUserId));
    }

    public static NotificationUserMismatchException forRead(Long notificationId, Long requestedUserId, Long actualUserId) {
        return new NotificationUserMismatchException("leer", notificationId, requestedUserId, actualUserId);
    }

    public static NotificationUserMismatchException forUpdate(Long notificationId, Long requestedUserId, Long actualUserId) {
        return new NotificationUserMismatchException("actualizar", notificationId, requestedUserId, actualUserId);
    }

    public static NotificationUserMismatchException forDelete(Long notificationId, Long requestedUserId, Long actualUserId) {
        return new NotificationUserMismatchException("eliminar", notificationId, requestedUserId, actualUserId);
    }
} 