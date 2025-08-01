package com.devmatch.api.usernotification.infrastructure.in.controller;

import com.devmatch.api.usernotification.application.dto.NotificationRequestDto;
import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.dto.NotificationStatusRequestDto;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import com.devmatch.api.usernotification.application.port.in.NotificationQueryUseCase;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para la gestión de notificaciones.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar notificaciones.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationManagementUseCase notificationManagementUseCase;
    private final NotificationQueryUseCase notificationQueryUseCase;

    // ==================== ENDPOINTS DE GESTIÓN ====================

    /**
     * Crea una nueva notificación.
     * 
     * @param request DTO con los datos de la notificación
     * @return Notificación creada
     */
    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@Valid @RequestBody NotificationRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de aplicación a proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Notificación creada
     */
    @PostMapping("/project-application")
    public ResponseEntity<NotificationResponseDto> createProjectApplicationNotification(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String projectTitle) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationNotification(userId, projectId, projectTitle);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de aplicación aceptada.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Notificación creada
     */
    @PostMapping("/project-application-accepted")
    public ResponseEntity<NotificationResponseDto> createProjectApplicationAcceptedNotification(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String projectTitle) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationAcceptedNotification(userId, projectId, projectTitle);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de aplicación rechazada.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param reason Razón del rechazo
     * @return Notificación creada
     */
    @PostMapping("/project-application-rejected")
    public ResponseEntity<NotificationResponseDto> createProjectApplicationRejectedNotification(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String projectTitle,
            @RequestParam String reason) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationRejectedNotification(userId, projectId, projectTitle, reason);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de nuevo miembro en proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param memberName Nombre del nuevo miembro
     * @return Notificación creada
     */
    @PostMapping("/project-member-joined")
    public ResponseEntity<NotificationResponseDto> createProjectMemberJoinedNotification(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String projectTitle,
            @RequestParam String memberName) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectMemberJoinedNotification(userId, projectId, projectTitle, memberName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de review recibida.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param reviewId ID de la review
     * @param reviewerName Nombre del revisor
     * @return Notificación creada
     */
    @PostMapping("/project-review-received")
    public ResponseEntity<NotificationResponseDto> createProjectReviewReceivedNotification(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam Long reviewId,
            @RequestParam String reviewerName) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectReviewReceivedNotification(userId, projectId, reviewId, reviewerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de logro desbloqueado.
     * 
     * @param userId ID del usuario
     * @param achievementCode Código del logro
     * @param achievementName Nombre del logro
     * @return Notificación creada
     */
    @PostMapping("/achievement-unlocked")
    public ResponseEntity<NotificationResponseDto> createAchievementUnlockedNotification(
            @RequestParam Long userId,
            @RequestParam String achievementCode,
            @RequestParam String achievementName) {
        NotificationResponseDto response = notificationManagementUseCase.createAchievementUnlockedNotification(userId, achievementCode, achievementName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de bienvenida.
     * 
     * @param userId ID del usuario
     * @param username Nombre de usuario
     * @return Notificación creada
     */
    @PostMapping("/welcome")
    public ResponseEntity<NotificationResponseDto> createWelcomeNotification(
            @RequestParam Long userId,
            @RequestParam String username) {
        NotificationResponseDto response = notificationManagementUseCase.createWelcomeNotification(userId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación del sistema.
     * 
     * @param userId ID del usuario
     * @param message Mensaje del sistema
     * @return Notificación creada
     */
    @PostMapping("/system")
    public ResponseEntity<NotificationResponseDto> createSystemNotification(
            @RequestParam Long userId,
            @RequestParam String message) {
        NotificationResponseDto response = notificationManagementUseCase.createSystemNotification(userId, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Marca una notificación como leída.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario
     * @return Notificación actualizada
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        NotificationResponseDto response = notificationManagementUseCase.markAsRead(notificationId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Marca múltiples notificaciones como leídas.
     * 
     * @param notificationIds Lista de IDs de notificaciones
     * @param userId ID del usuario
     * @return Lista de notificaciones marcadas como leídas
     */
    @PutMapping("/read-multiple")
    public ResponseEntity<List<NotificationResponseDto>> markMultipleAsRead(
            @RequestBody List<Long> notificationIds,
            @RequestParam Long userId) {
        List<NotificationResponseDto> notifications = notificationManagementUseCase.markMultipleAsRead(notificationIds, userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marca todas las notificaciones del usuario como leídas.
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones marcadas como leídas
     */
    @PutMapping("/read-all")
    public ResponseEntity<Integer> markAllAsRead(@RequestParam Long userId) {
        int count = notificationManagementUseCase.markAllAsRead(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Actualiza el estado de una notificación.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario
     * @param statusRequest DTO con los cambios de estado
     * @return Notificación actualizada
     */
    @PutMapping("/{notificationId}/status")
    public ResponseEntity<NotificationResponseDto> updateNotificationStatus(
            @PathVariable Long notificationId,
            @RequestParam Long userId,
            @Valid @RequestBody NotificationStatusRequestDto statusRequest) {
        NotificationResponseDto response = notificationManagementUseCase.updateNotificationStatus(notificationId, userId, statusRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una notificación.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario
     * @return Respuesta de confirmación
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        notificationManagementUseCase.deleteNotification(notificationId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples notificaciones.
     * 
     * @param notificationIds Lista de IDs de notificaciones
     * @param userId ID del usuario
     * @return Número de notificaciones eliminadas
     */
    @DeleteMapping("/multiple")
    public ResponseEntity<Integer> deleteMultipleNotifications(
            @RequestBody List<Long> notificationIds,
            @RequestParam Long userId) {
        int count = notificationManagementUseCase.deleteMultipleNotifications(notificationIds, userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Elimina todas las notificaciones del usuario.
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones eliminadas
     */
    @DeleteMapping("/all")
    public ResponseEntity<Integer> deleteAllUserNotifications(@RequestParam Long userId) {
        int count = notificationManagementUseCase.deleteAllUserNotifications(userId);
        return ResponseEntity.ok(count);
    }

    // ==================== ENDPOINTS DE CONSULTA ====================

    /**
     * Obtiene una notificación por ID.
     * 
     * @param notificationId ID de la notificación
     * @param userId ID del usuario
     * @return Notificación encontrada
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        return notificationQueryUseCase.getNotificationById(notificationId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las notificaciones del usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getAllUserNotifications(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getAllUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones paginada
     */
    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsPaginated(userId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones no leídas del usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones no leídas
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadUserNotifications(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUnreadUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones no leídas del usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones no leídas paginada
     */
    @GetMapping("/user/{userId}/unread/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadUserNotificationsPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUnreadUserNotificationsPaginated(userId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por tipo.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @return Lista de notificaciones del tipo especificado
     */
    @GetMapping("/user/{userId}/type/{notificationType}")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByType(
            @PathVariable Long userId,
            @PathVariable String notificationType) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByType(userId, notificationType);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por tipo con paginación.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones del tipo especificado paginada
     */
    @GetMapping("/user/{userId}/type/{notificationType}/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByTypePaginated(
            @PathVariable Long userId,
            @PathVariable String notificationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByTypePaginated(userId, notificationType, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @return Lista de notificaciones del proyecto
     */
    @GetMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByProject(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByProject(userId, projectId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por proyecto con paginación.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones del proyecto paginada
     */
    @GetMapping("/user/{userId}/project/{projectId}/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByProjectPaginated(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByProjectPaginated(userId, projectId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por review.
     * 
     * @param userId ID del usuario
     * @param reviewId ID de la review
     * @return Lista de notificaciones de la review
     */
    @GetMapping("/user/{userId}/review/{reviewId}")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByReview(
            @PathVariable Long userId,
            @PathVariable Long reviewId) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByReview(userId, reviewId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por review con paginación.
     * 
     * @param userId ID del usuario
     * @param reviewId ID de la review
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones de la review paginada
     */
    @GetMapping("/user/{userId}/review/{reviewId}/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByReviewPaginated(
            @PathVariable Long userId,
            @PathVariable Long reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByReviewPaginated(userId, reviewId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones de logros del usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de notificaciones de logros
     */
    @GetMapping("/user/{userId}/achievements")
    public ResponseEntity<List<NotificationResponseDto>> getUserAchievementNotifications(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserAchievementNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones de logros del usuario con paginación.
     * 
     * @param userId ID del usuario
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones de logros paginada
     */
    @GetMapping("/user/{userId}/achievements/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserAchievementNotificationsPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserAchievementNotificationsPaginated(userId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por rango de fechas.
     * 
     * @param userId ID del usuario
     * @param startDate Fecha de inicio (formato: yyyy-MM-dd HH:mm:ss)
     * @param endDate Fecha de fin (formato: yyyy-MM-dd HH:mm:ss)
     * @return Lista de notificaciones en el rango de fechas
     */
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario por rango de fechas con paginación.
     * 
     * @param userId ID del usuario
     * @param startDate Fecha de inicio (formato: yyyy-MM-dd HH:mm:ss)
     * @param endDate Fecha de fin (formato: yyyy-MM-dd HH:mm:ss)
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones en el rango de fechas paginada
     */
    @GetMapping("/user/{userId}/date-range/paginated")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotificationsByDateRangePaginated(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByDateRangePaginated(userId, startDate, endDate, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificaciones del usuario por texto en el mensaje.
     * 
     * @param userId ID del usuario
     * @param searchText Texto a buscar
     * @return Lista de notificaciones que contienen el texto
     */
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<NotificationResponseDto>> searchUserNotifications(
            @PathVariable Long userId,
            @RequestParam String searchText) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.searchUserNotifications(userId, searchText);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificaciones del usuario por texto en el mensaje con paginación.
     * 
     * @param userId ID del usuario
     * @param searchText Texto a buscar
     * @param page Número de página (0-based)
     * @param size Tamaño de la página
     * @return Lista de notificaciones que contienen el texto paginada
     */
    @GetMapping("/user/{userId}/search/paginated")
    public ResponseEntity<List<NotificationResponseDto>> searchUserNotificationsPaginated(
            @PathVariable Long userId,
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.searchUserNotificationsPaginated(userId, searchText, page, size);
        return ResponseEntity.ok(notifications);
    }

    // ==================== ENDPOINTS DE ESTADÍSTICAS ====================

    /**
     * Obtiene el conteo total de notificaciones del usuario.
     * 
     * @param userId ID del usuario
     * @return Número total de notificaciones
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUserNotifications(@PathVariable Long userId) {
        long count = notificationQueryUseCase.countUserNotifications(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene el conteo de notificaciones no leídas del usuario.
     * 
     * @param userId ID del usuario
     * @return Número de notificaciones no leídas
     */
    @GetMapping("/user/{userId}/count/unread")
    public ResponseEntity<Long> countUnreadUserNotifications(@PathVariable Long userId) {
        long count = notificationQueryUseCase.countUnreadUserNotifications(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene el conteo de notificaciones del usuario por tipo.
     * 
     * @param userId ID del usuario
     * @param notificationType Tipo de notificación
     * @return Número de notificaciones del tipo especificado
     */
    @GetMapping("/user/{userId}/count/type/{notificationType}")
    public ResponseEntity<Long> countUserNotificationsByType(
            @PathVariable Long userId,
            @PathVariable String notificationType) {
        long count = notificationQueryUseCase.countUserNotificationsByType(userId, notificationType);
        return ResponseEntity.ok(count);
    }
} 