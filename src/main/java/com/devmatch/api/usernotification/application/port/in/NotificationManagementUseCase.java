package com.devmatch.api.usernotification.application.port.in;

import com.devmatch.api.usernotification.application.dto.NotificationRequestDto;
import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.dto.NotificationStatusRequestDto;

import java.util.List;

/**
 * Puerto de entrada para las operaciones de gestión de notificaciones.
 * Define los casos de uso para crear, actualizar y eliminar notificaciones.
 */
public interface NotificationManagementUseCase {

    /**
     * Crea una nueva notificación.
     * 
     * @param request DTO con los datos de la notificación a crear
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createNotification(NotificationRequestDto request);

    /**
     * Crea una notificación de aplicación a proyecto.
     * 
     * @param userId ID del usuario que aplicó al proyecto
     * @param projectId ID del proyecto
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createProjectApplicationNotification(Long userId, Long projectId);

    /**
     * Crea una notificación de aplicación aceptada.
     * 
     * @param userId ID del usuario cuya aplicación fue aceptada
     * @param projectId ID del proyecto
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createProjectApplicationAcceptedNotification(Long userId, Long projectId);

    /**
     * Crea una notificación de aplicación rechazada.
     * 
     * @param userId ID del usuario cuya aplicación fue rechazada
     * @param projectId ID del proyecto
     * @param reason Razón del rechazo (opcional)
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createProjectApplicationRejectedNotification(Long userId, Long projectId, String reason);

    /**
     * Crea una notificación de nuevo miembro en proyecto.
     * 
     * @param userId ID del usuario que se unió al proyecto
     * @param projectId ID del proyecto
     * @param memberName Nombre del nuevo miembro
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createProjectMemberJoinedNotification(Long userId, Long projectId, String memberName);

    /**
     * Crea una notificación de review recibida.
     * 
     * @param userId ID del usuario que recibió la review
     * @param projectId ID del proyecto
     * @param reviewId ID de la review
     * @param reviewerName Nombre del revisor
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createProjectReviewReceivedNotification(Long userId, Long projectId, Long reviewId, String reviewerName);

    /**
     * Crea una notificación de logro desbloqueado.
     * 
     * @param userId ID del usuario que desbloqueó el logro
     * @param achievementCode Código del logro
     * @param achievementName Nombre del logro
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createAchievementUnlockedNotification(Long userId, String achievementCode, String achievementName);

    /**
     * Crea una notificación de bienvenida.
     * 
     * @param userId ID del usuario
     * @return DTO con la notificación creada
     */
    NotificationResponseDto createWelcomeNotification(Long userId);

    /**
     * Marca una notificación como leída.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario que realiza la acción
     * @return DTO con la notificación actualizada
     */
    NotificationResponseDto markAsRead(Long notificationId, Long userId);

    /**
     * Marca múltiples notificaciones como leídas.
     * 
     * @param notificationIds Lista de IDs de notificaciones
     * @param userId ID del usuario que realiza la acción
     * @return Lista de DTOs con las notificaciones actualizadas
     */
    List<NotificationResponseDto> markMultipleAsRead(List<Long> notificationIds, Long userId);

    /**
     * Marca todas las notificaciones de un usuario como leídas.
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones marcadas como leídas
     */
    int markAllAsRead(Long userId);

    /**
     * Actualiza el estado de una notificación.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario que realiza la acción
     * @param statusRequest DTO con el nuevo estado
     * @return DTO con la notificación actualizada
     */
    NotificationResponseDto updateNotificationStatus(Long notificationId, Long userId, NotificationStatusRequestDto statusRequest);

    /**
     * Elimina una notificación (marcado lógico).
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario que realiza la acción
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean deleteNotification(Long notificationId, Long userId);

    /**
     * Elimina múltiples notificaciones (marcado lógico).
     * 
     * @param notificationIds Lista de IDs de notificaciones
     * @param userId ID del usuario que realiza la acción
     * @return Número de notificaciones eliminadas
     */
    int deleteMultipleNotifications(List<Long> notificationIds, Long userId);

    /**
     * Elimina todas las notificaciones de un usuario (marcado lógico).
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones eliminadas
     */
    int deleteAllUserNotifications(Long userId);
} 