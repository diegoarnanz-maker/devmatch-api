package com.devmatch.api.usernotification.domain.service;

import com.devmatch.api.usernotification.domain.model.Notification;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationMessage;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio para la gestión de notificaciones.
 * Encapsula la lógica de negocio compleja relacionada con las notificaciones.
 */
public class NotificationDomainService {

    /**
     * Crea una notificación de aplicación a proyecto.
     * 
     * @param userId ID del usuario que aplicó al proyecto
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Notificación creada
     */
    public Notification createProjectApplicationNotification(Long userId, Long projectId, String projectTitle) {
        NotificationMessage message = new NotificationMessage(
            "Has aplicado al proyecto: " + projectTitle
        );
        
        return new Notification(
            null, userId, message, NotificationType.PROJECT_APPLICATION,
            projectId, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de aplicación aceptada.
     * 
     * @param userId ID del usuario cuya aplicación fue aceptada
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Notificación creada
     */
    public Notification createProjectApplicationAcceptedNotification(Long userId, Long projectId, String projectTitle) {
        NotificationMessage message = new NotificationMessage(
            "¡Tu aplicación al proyecto '" + projectTitle + "' ha sido aceptada!"
        );
        
        return new Notification(
            null, userId, message, NotificationType.PROJECT_APPLICATION_ACCEPTED,
            projectId, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de aplicación rechazada.
     * 
     * @param userId ID del usuario cuya aplicación fue rechazada
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param reason Razón del rechazo (opcional)
     * @return Notificación creada
     */
    public Notification createProjectApplicationRejectedNotification(Long userId, Long projectId, String projectTitle, String reason) {
        String messageText = "Tu aplicación al proyecto '" + projectTitle + "' ha sido rechazada";
        if (reason != null && !reason.trim().isEmpty()) {
            messageText += ". Razón: " + reason;
        }
        
        NotificationMessage message = new NotificationMessage(messageText);
        
        return new Notification(
            null, userId, message, NotificationType.PROJECT_APPLICATION_REJECTED,
            projectId, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de nuevo miembro en proyecto.
     * 
     * @param userId ID del usuario que se unió al proyecto
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param memberName Nombre del nuevo miembro
     * @return Notificación creada
     */
    public Notification createProjectMemberJoinedNotification(Long userId, Long projectId, String projectTitle, String memberName) {
        NotificationMessage message = new NotificationMessage(
            memberName + " se ha unido al proyecto '" + projectTitle + "'"
        );
        
        return new Notification(
            null, userId, message, NotificationType.PROJECT_MEMBER_JOINED,
            projectId, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de review recibida.
     * 
     * @param userId ID del usuario que recibió la review
     * @param projectId ID del proyecto
     * @param reviewId ID de la review
     * @param reviewerName Nombre del revisor
     * @return Notificación creada
     */
    public Notification createProjectReviewReceivedNotification(Long userId, Long projectId, Long reviewId, String reviewerName) {
        NotificationMessage message = new NotificationMessage(
            "Has recibido una nueva review de " + reviewerName
        );
        
        return new Notification(
            null, userId, message, NotificationType.PROJECT_REVIEW_RECEIVED,
            projectId, reviewId, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de logro desbloqueado.
     * 
     * @param userId ID del usuario que desbloqueó el logro
     * @param achievementCode Código del logro
     * @param achievementName Nombre del logro
     * @return Notificación creada
     */
    public Notification createAchievementUnlockedNotification(Long userId, String achievementCode, String achievementName) {
        NotificationMessage message = new NotificationMessage(
            "¡Has desbloqueado el logro: " + achievementName + "!"
        );
        
        return new Notification(
            null, userId, message, NotificationType.ACHIEVEMENT_UNLOCKED,
            null, null, achievementCode, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de bienvenida.
     * 
     * @param userId ID del usuario
     * @param username Nombre de usuario
     * @return Notificación creada
     */
    public Notification createWelcomeNotification(Long userId, String username) {
        NotificationMessage message = new NotificationMessage(
            "¡Bienvenido a DevMatch, " + username + "! Estamos emocionados de tenerte con nosotros."
        );
        
        return new Notification(
            null, userId, message, NotificationType.WELCOME_MESSAGE,
            null, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación del sistema.
     * 
     * @param userId ID del usuario destinatario
     * @param message Mensaje del sistema
     * @return Notificación creada
     */
    public Notification createSystemNotification(Long userId, String message) {
        NotificationMessage notificationMessage = new NotificationMessage(message);
        
        return new Notification(
            null, userId, notificationMessage, NotificationType.SYSTEM_MESSAGE,
            null, null, null, false, true, false,
            LocalDateTime.now(), LocalDateTime.now()
        );
    }

    /**
     * Valida si se puede crear una notificación para un usuario.
     * 
     * @param userId ID del usuario
     * @param existingNotifications Lista de notificaciones existentes del usuario
     * @param maxNotifications Número máximo de notificaciones permitidas
     * @return true si se puede crear, false en caso contrario
     */
    public boolean canCreateNotification(Long userId, List<Notification> existingNotifications, int maxNotifications) {
        if (existingNotifications == null) {
            return true;
        }
        
        long activeNotifications = existingNotifications.stream()
            .filter(Notification::isActive)
            .filter(n -> !n.isDeleted())
            .count();
            
        return activeNotifications < maxNotifications;
    }

    /**
     * Determina la prioridad de una notificación basada en su tipo.
     * 
     * @param notificationType Tipo de notificación
     * @return Prioridad (1 = alta, 2 = media, 3 = baja)
     */
    public int determineNotificationPriority(NotificationType notificationType) {
        switch (notificationType) {
            case PROJECT_APPLICATION_ACCEPTED:
            case PROJECT_APPLICATION_REJECTED:
            case ACHIEVEMENT_UNLOCKED:
                return 1; // Alta prioridad
                
            case PROJECT_REVIEW_RECEIVED:
            case PROJECT_MEMBER_JOINED:
            case PROJECT_MEMBER_LEFT:
                return 2; // Media prioridad
                
            case PROJECT_APPLICATION:
            case PROJECT_REVIEW_RESPONSE:
            case SYSTEM_MESSAGE:
            case WELCOME_MESSAGE:
            default:
                return 3; // Baja prioridad
        }
    }

    /**
     * Valida si una notificación puede ser marcada como leída.
     * 
     * @param notification Notificación a validar
     * @return true si puede ser marcada como leída, false en caso contrario
     */
    public boolean canMarkAsRead(Notification notification) {
        return notification != null && 
               notification.isActive() && 
               !notification.isDeleted() && 
               !notification.isRead();
    }

    /**
     * Valida si una notificación puede ser eliminada.
     * 
     * @param notification Notificación a validar
     * @return true si puede ser eliminada, false en caso contrario
     */
    public boolean canDeleteNotification(Notification notification) {
        return notification != null && 
               notification.isActive() && 
               !notification.isDeleted();
    }

    /**
     * Verifica si existe una notificación duplicada reciente.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @param relatedId ID relacionado (projectId, reviewId, etc.)
     * @param existingNotifications Lista de notificaciones existentes
     * @param timeWindowMinutes Ventana de tiempo en minutos para considerar duplicados
     * @return true si existe duplicado, false en caso contrario
     */
    public boolean hasRecentDuplicate(Long userId, NotificationType notificationType, Long relatedId, 
                                    List<Notification> existingNotifications, int timeWindowMinutes) {
        if (existingNotifications == null) {
            return false;
        }
        
        LocalDateTime timeWindow = LocalDateTime.now().minusMinutes(timeWindowMinutes);
        
        return existingNotifications.stream()
            .filter(n -> n.getUserId().equals(userId))
            .filter(n -> n.getNotificationType() == notificationType)
            .filter(n -> n.isActive() && !n.isDeleted())
            .filter(n -> n.getCreatedAt().isAfter(timeWindow))
            .anyMatch(n -> {
                if (relatedId != null) {
                    return relatedId.equals(n.getProjectId()) || 
                           relatedId.equals(n.getReviewId());
                }
                return true;
            });
    }
} 