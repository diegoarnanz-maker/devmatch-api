package com.devmatch.api.usernotification.infrastructure.in.controller;

import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.dto.NotificationRejectionRequestDto;

import com.devmatch.api.usernotification.application.dto.ProjectMemberJoinedRequestDto;
import com.devmatch.api.usernotification.application.dto.ProjectReviewReceivedRequestDto;
import com.devmatch.api.usernotification.application.dto.AchievementUnlockedRequestDto;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import com.devmatch.api.usernotification.application.port.in.NotificationQueryUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "notification-controller", description = "Endpoints para gestión de notificaciones del sistema")
public class NotificationController {

    private final NotificationManagementUseCase notificationManagementUseCase;
    private final NotificationQueryUseCase notificationQueryUseCase;

    @Operation(summary = "Crear notificación de aplicación a proyecto (Interno)", description = "Crea una notificación de aplicación a proyecto (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-application/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de aplicación aceptada (Interno)", description = "Crea una notificación de aplicación aceptada (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-application-accepted/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationAcceptedNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationAcceptedNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de aplicación rechazada (Interno)", description = "Crea una notificación de aplicación rechazada (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-application-rejected/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationRejectedNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Datos del rechazo")
            @Valid @RequestBody NotificationRejectionRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationRejectedNotification(userId, projectId, request.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de nuevo miembro (Interno)", description = "Crea una notificación de nuevo miembro en proyecto (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-member-joined/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectMemberJoinedNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Datos del miembro")
            @Valid @RequestBody ProjectMemberJoinedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectMemberJoinedNotification(userId, projectId, request.getMemberName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de aplicación cancelada (Interno)", description = "Crea una notificación de aplicación cancelada (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-application-cancelled/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationCancelledNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationCancelledNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de aplicación expirada (Interno)", description = "Crea una notificación de aplicación expirada (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o proyecto no encontrado")
    })
    @PostMapping("/internal/project-application-expired/{userId}/{projectId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectApplicationExpiredNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectApplicationExpiredNotification(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de review recibida (Interno)", description = "Crea una notificación de review recibida (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario, proyecto o review no encontrado")
    })
    @PostMapping("/internal/project-review-received/{userId}/{projectId}/{reviewId}")
    public ResponseEntity<NotificationResponseDto> createInternalProjectReviewReceivedNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del review", example = "1")
            @PathVariable Long reviewId,
            @Parameter(description = "Datos del review")
            @Valid @RequestBody ProjectReviewReceivedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createProjectReviewReceivedNotification(userId, projectId, reviewId, request.getReviewerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de logro desbloqueado (Interno)", description = "Crea una notificación de logro desbloqueado (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario o logro no encontrado")
    })
    @PostMapping("/internal/achievement-unlocked/{userId}/{achievementCode}")
    public ResponseEntity<NotificationResponseDto> createInternalAchievementUnlockedNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Código del logro", example = "FIRST_PROJECT")
            @PathVariable String achievementCode,
            @Parameter(description = "Datos del logro")
            @Valid @RequestBody AchievementUnlockedRequestDto request) {
        NotificationResponseDto response = notificationManagementUseCase.createAchievementUnlockedNotification(userId, achievementCode, request.getAchievementName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear notificación de bienvenida (Interno)", description = "Crea una notificación de bienvenida (llamada interna del sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/internal/welcome/{userId}")
    public ResponseEntity<NotificationResponseDto> createInternalWelcomeNotification(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        NotificationResponseDto response = notificationManagementUseCase.createWelcomeNotification(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener mis notificaciones", description = "Obtiene las notificaciones del usuario autenticado con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-notifications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsPaginated(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Obtener mis notificaciones no leídas", description = "Obtiene las notificaciones no leídas del usuario autenticado con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones no leídas obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-notifications/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyUnreadNotificationsPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUnreadUserNotificationsPaginated(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Obtener mis notificaciones por tipo", description = "Obtiene las notificaciones del usuario autenticado por tipo con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones por tipo obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-notifications/type/{notificationType}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByTypePaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Tipo de notificación", example = "PROJECT_APPLICATION")
            @PathVariable String notificationType,
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByTypePaginated(userPrincipal.getUserId(), notificationType, page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Obtener mis notificaciones por proyecto", description = "Obtiene las notificaciones del usuario autenticado por proyecto con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones por proyecto obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-notifications/project/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationsByProjectPaginated(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDto> notifications = notificationQueryUseCase.getUserNotificationsByProjectPaginated(userPrincipal.getUserId(), projectId, page, size);
        return ResponseEntity.ok(notifications);
    }



    @Operation(summary = "Obtener notificación por ID", description = "Obtiene una notificación específica del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationResponseDto> getMyNotificationById(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId) {
        return notificationQueryUseCase.getNotificationById(notificationId, userPrincipal.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Marcar notificación como leída", description = "Marca una notificación del usuario autenticado como leída")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationResponseDto> markMyNotificationAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId) {
        NotificationResponseDto response = notificationManagementUseCase.markAsRead(notificationId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar múltiples notificaciones como leídas", description = "Marca múltiples notificaciones del usuario autenticado como leídas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones marcadas como leídas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/read-multiple")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponseDto>> markMyMultipleNotificationsAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Lista de IDs de notificaciones")
            @RequestBody List<Long> notificationIds) {
        List<NotificationResponseDto> notifications = notificationManagementUseCase.markMultipleAsRead(notificationIds, userPrincipal.getUserId());
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Marcar todas las notificaciones como leídas", description = "Marca todas las notificaciones del usuario autenticado como leídas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todas las notificaciones marcadas como leídas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/read-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> markAllMyNotificationsAsRead(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        int count = notificationManagementUseCase.markAllAsRead(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMyNotification(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId) {
        notificationManagementUseCase.deleteNotification(notificationId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar múltiples notificaciones", description = "Elimina múltiples notificaciones del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones eliminadas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/multiple")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> deleteMyMultipleNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Lista de IDs de notificaciones")
            @RequestBody List<Long> notificationIds) {
        int count = notificationManagementUseCase.deleteMultipleNotifications(notificationIds, userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Eliminar todas las notificaciones", description = "Elimina todas las notificaciones del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todas las notificaciones eliminadas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo usuarios autenticados pueden acceder")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> deleteAllMyNotifications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        int count = notificationManagementUseCase.deleteAllUserNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

} 