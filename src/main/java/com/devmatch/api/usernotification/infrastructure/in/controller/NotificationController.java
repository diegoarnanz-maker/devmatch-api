package com.devmatch.api.usernotification.infrastructure.in.controller;

import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.dto.NotificationRejectionRequestDto;
import com.devmatch.api.usernotification.application.dto.NotificationStatusRequestDto;
import com.devmatch.api.usernotification.application.dto.ProjectMemberJoinedRequestDto;
import com.devmatch.api.usernotification.application.dto.ProjectReviewReceivedRequestDto;
import com.devmatch.api.usernotification.application.dto.AchievementUnlockedRequestDto;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import com.devmatch.api.usernotification.application.port.in.NotificationQueryUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de notificaciones.
 * Proporciona endpoints internos para el sistema y endpoints protegidos para usuarios.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationManagementUseCase notificationManagementUseCase;
    private final NotificationQueryUseCase notificationQueryUseCase;

    // ==================== ENDPOINTS INTERNOS (SISTEMA) ====================

    /**
     * Crea una notificación de aplicación a proyecto (llamada interna del sistema).
     */
    @PostMapping("/internal/project-application/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationNotification(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de aplicación aceptada (llamada interna del sistema).
     */
    @PostMapping("/internal/project-application-accepted/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationAcceptedNotification(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationAcceptedNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de aplicación rechazada (llamada interna del sistema).
     */
    @PostMapping("/internal/project-application-rejected/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationRejectedNotification(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @Valid @RequestBody NotificationRejectionRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationRejectedNotification(userId, projectId, request.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de nuevo miembro en proyecto (llamada interna del sistema).
     */
    @PostMapping("/internal/project-member-joined/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectMemberJoinedNotification(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberJoinedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectMemberJoinedNotification(userId, projectId, request.getMemberName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de review recibida (llamada interna del sistema).
     */
    @PostMapping("/internal/project-review-received/{userId}/{projectId}/{reviewId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectReviewReceivedNotification(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ProjectReviewReceivedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectReviewReceivedNotification(userId, projectId, reviewId, request.getReviewerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de logro desbloqueado (llamada interna del sistema).
     */
    @PostMapping("/internal/achievement-unlocked/{userId}/{achievementCode}")
    public ResponseEntity<NotificationResponseDto> createInternalAchievementUnlockedNotification(
            @PathVariable Long userId,
            @PathVariable String achievementCode,
            @Valid @RequestBody AchievementUnlockedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createAchievementUnlockedNotification(userId, achievementCode, request.getAchievementName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación de bienvenida (llamada interna del sistema).
     */
    @PostMapping("/internal/welcome/{userId}")
    public ResponseEntity<NotificationResponseDto> createInternalWelcomeNotification(
            @PathVariable Long userId) {
        NotificationResponseDto response = notificationManagementUseCase.createWelcomeNotification(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea una notificación del sistema (llamada interna del sistema).
     */
    @PostMapping("/internal/system/{userId}")
    public ResponseEntity<NotificationResponseDto> createInternalSystemNotification(
            @PathVariable Long userId,
            @RequestParam String message) {
        NotificationResponseDto response = notificationManagementUseCase.createSystemNotification(userId, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== ENDPOINTS DE USUARIO (PROTEGIDOS) ====================

    /**
     * Obtiene las notificaciones del usuario autenticado con paginación.
     */
    @GetMapping("/my-notifications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsPaginated(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones no leídas del usuario autenticado con paginación.
     */
    @GetMapping("/my-notifications/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyUnreadNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUnreadUserNotificationsPaginated(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario autenticado por tipo con paginación.
     */
    @GetMapping("/my-notifications/type/{notificationType}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByTypePaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable String notificationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByTypePaginated(userPrincipal.getUserId(), notificationType, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario autenticado por proyecto con paginación.
     */
    @GetMapping("/my-notifications/project/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByProjectPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByProjectPaginated(userPrincipal.getUserId(), projectId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario autenticado por review con paginación.
     */
    @GetMapping("/my-notifications/review/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByReviewPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByReviewPaginated(userPrincipal.getUserId(), reviewId, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones de logros del usuario autenticado con paginación.
     */
    @GetMapping("/my-notifications/achievements")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyAchievementNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserAchievementNotificationsPaginated(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene las notificaciones del usuario autenticado por rango de fechas con paginación.
     */
    @GetMapping("/my-notifications/date-range")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByDateRangePaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByDateRangePaginated(userPrincipal.getUserId(), startDate, endDate, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificaciones del usuario autenticado por texto con paginación.
     */
    @GetMapping("/my-notifications/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> searchMyNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.searchUserNotificationsPaginated(userPrincipal.getUserId(), searchText, page, size);
        return ResponseEntity.ok(notifications);
    }

    // ==================== ENDPOINTS DE GESTIÓN DE USUARIO ====================

    /**
     * Obtiene una notificación específica del usuario autenticado.
     */
    @GetMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationResponseDto> getMyNotificationById(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long notificationId) {
        return notificationQueryUseCase.getNotificationById(notificationId, userPrincipal.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Marca una notificación del usuario autenticado como leída.
     */
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationResponseDto> markMyNotificationAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long notificationId) {
        NotificationResponseDto response = notificationManagementUseCase.markAsRead(notificationId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Marca múltiples notificaciones del usuario autenticado como leídas.
     */
    @PutMapping("/read-multiple")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> markMyMultipleNotificationsAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestBody List<Long> notificationIds) {
        List<NotificationResponseDto> notifications = notificationManagementUseCase.markMultipleAsRead(notificationIds, userPrincipal.getUserId());
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marca todas las notificaciones del usuario autenticado como leídas.
     */
    @PutMapping("/read-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> markAllMyNotificationsAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        int count = notificationManagementUseCase.markAllAsRead(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    /**
     * Actualiza el estado de una notificación del usuario autenticado.
     */
    @PutMapping("/{notificationId}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationResponseDto> updateMyNotificationStatus(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long notificationId,
            @Valid @RequestBody NotificationStatusRequestDto statusRequest) {
        NotificationResponseDto response = notificationManagementUseCase.updateNotificationStatus(notificationId, userPrincipal.getUserId(), statusRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una notificación del usuario autenticado.
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMyNotification(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long notificationId) {
        notificationManagementUseCase.deleteNotification(notificationId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples notificaciones del usuario autenticado.
     */
    @DeleteMapping("/multiple")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> deleteMyMultipleNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @RequestBody List<Long> notificationIds) {
        int count = notificationManagementUseCase.deleteMultipleNotifications(notificationIds, userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    /**
     * Elimina todas las notificaciones del usuario autenticado.
     */
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> deleteAllMyNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        int count = notificationManagementUseCase.deleteAllUserNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    // ==================== ENDPOINTS DE ESTADÍSTICAS DE USUARIO ====================

    /**
     * Obtiene el conteo total de notificaciones del usuario autenticado.
     */
    @GetMapping("/my-notifications/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> countMyNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        long count = notificationQueryUseCase.countUserNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene el conteo de notificaciones no leídas del usuario autenticado.
     */
    @GetMapping("/my-notifications/count/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> countMyUnreadNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        long count = notificationQueryUseCase.countUnreadUserNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene el conteo de notificaciones del usuario autenticado por tipo.
     */
    @GetMapping("/my-notifications/count/type/{notificationType}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> countMyNotificationsByType(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable String notificationType) {
        long count = notificationQueryUseCase.countUserNotificationsByType(userPrincipal.getUserId(), notificationType);
        return ResponseEntity.ok(count);
    }
} 