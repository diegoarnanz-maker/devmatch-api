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