package com.devmatch.api.usernotification.application.port.in;

import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para las operaciones de consulta de notificaciones.
 * Define los casos de uso para buscar, listar y filtrar notificaciones.
 */
public interface NotificationQueryUseCase {

    /**
     * Obtiene una notificación por su ID.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario que solicita la notificación
     * @return Optional con la notificación si existe y pertenece al usuario
     */
    Optional<NotificationResponseDto> getNotificationById(Long notificationId, Long userId);

    /**
     * Obtiene todas las notificaciones de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    List<NotificationResponseDto> getAllUserNotifications(Long userId);

    /**
     * Obtiene las notificaciones de un usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones del usuario
     */
    List<NotificationResponseDto> getUserNotificationsPaginated(Long userId, int page, int size);

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones no leídas del usuario
     */
    List<NotificationResponseDto> getUnreadUserNotifications(Long userId);

    /**
     * Obtiene las notificaciones no leídas de un usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones no leídas del usuario
     */
    List<NotificationResponseDto> getUnreadUserNotificationsPaginated(Long userId, int page, int size);

    /**
     * Obtiene las notificaciones de un usuario por tipo.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @return Lista de notificaciones del tipo especificado
     */
    List<NotificationResponseDto> getUserNotificationsByType(Long userId, String notificationType);

    /**
     * Obtiene las notificaciones de un usuario por tipo con paginación.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones del tipo especificado
     */
    List<NotificationResponseDto> getUserNotificationsByTypePaginated(Long userId, String notificationType, int page, int size);

    /**
     * Obtiene las notificaciones de un usuario relacionadas con un proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @return Lista de notificaciones relacionadas con el proyecto
     */
    List<NotificationResponseDto> getUserNotificationsByProject(Long userId, Long projectId);

    /**
     * Obtiene las notificaciones de un usuario relacionadas con un proyecto con paginación.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones relacionadas con el proyecto
     */
    List<NotificationResponseDto> getUserNotificationsByProjectPaginated(Long userId, Long projectId, int page, int size);

    /**
     * Obtiene las notificaciones de un usuario relacionadas con una review.
     * 
     * @param userId ID del usuario
     * @param reviewId ID de la review
     * @return Lista de notificaciones relacionadas con la review
     */
    List<NotificationResponseDto> getUserNotificationsByReview(Long userId, Long reviewId);

    /**
     * Obtiene las notificaciones de un usuario relacionadas con una review con paginación.
     * 
     * @param userId ID del usuario
     * @param reviewId ID de la review
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones relacionadas con la review
     */
    List<NotificationResponseDto> getUserNotificationsByReviewPaginated(Long userId, Long reviewId, int page, int size);

    /**
     * Obtiene las notificaciones de logros de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones de logros del usuario
     */
    List<NotificationResponseDto> getUserAchievementNotifications(Long userId);

    /**
     * Obtiene las notificaciones de logros de un usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones de logros del usuario
     */
    List<NotificationResponseDto> getUserAchievementNotificationsPaginated(Long userId, int page, int size);

    /**
     * Obtiene las notificaciones de un usuario en un rango de fechas.
     * 
     * @param userId ID del usuario
     * @param startDate Fecha de inicio (inclusive)
     * @param endDate Fecha de fin (inclusive)
     * @return Lista de notificaciones en el rango de fechas
     */
    List<NotificationResponseDto> getUserNotificationsByDateRange(Long userId, String startDate, String endDate);

    /**
     * Obtiene las notificaciones de un usuario en un rango de fechas con paginación.
     * 
     * @param userId ID del usuario
     * @param startDate Fecha de inicio (inclusive)
     * @param endDate Fecha de fin (inclusive)
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones en el rango de fechas
     */
    List<NotificationResponseDto> getUserNotificationsByDateRangePaginated(Long userId, String startDate, String endDate, int page, int size);

    /**
     * Cuenta el total de notificaciones de un usuario.
     * 
     * @param userId ID del usuario
     * @return Número total de notificaciones del usuario
     */
    long countUserNotifications(Long userId);

    /**
     * Cuenta las notificaciones no leídas de un usuario.
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones no leídas del usuario
     */
    long countUnreadUserNotifications(Long userId);

    /**
     * Cuenta las notificaciones de un usuario por tipo.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @return Número de notificaciones del tipo especificado
     */
    long countUserNotificationsByType(Long userId, String notificationType);

    /**
     * Busca notificaciones de un usuario por texto en el mensaje.
     * 
     * @param userId ID del usuario
     * @param searchText Texto a buscar
     * @return Lista de notificaciones que contienen el texto buscado
     */
    List<NotificationResponseDto> searchUserNotifications(Long userId, String searchText);

    /**
     * Busca notificaciones de un usuario por texto en el mensaje con paginación.
     * 
     * @param userId ID del usuario
     * @param searchText Texto a buscar
     * @param page Número de página (base 0)
     * @param size Tamaño de la página
     * @return Lista paginada de notificaciones que contienen el texto buscado
     */
    List<NotificationResponseDto> searchUserNotificationsPaginated(Long userId, String searchText, int page, int size);
}