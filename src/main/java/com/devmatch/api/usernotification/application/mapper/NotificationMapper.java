package com.devmatch.api.usernotification.application.mapper;

import com.devmatch.api.usernotification.application.dto.NotificationRequestDto;
import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;

import com.devmatch.api.usernotification.domain.model.Notification;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationMessage;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de notificaciones.
 */
@Component
@RequiredArgsConstructor
public class NotificationMapper {

    /**
     * Convierte un DTO de solicitud a una entidad de dominio.
     * 
     * @param requestDto DTO de solicitud
     * @return Entidad de dominio
     */
    public Notification toDomain(NotificationRequestDto requestDto) {
        return new Notification(
            null, // ID se asignará al guardar
            requestDto.getUserId(),
            new NotificationMessage(requestDto.getMessage()),
            requestDto.getNotificationType(),
            requestDto.getProjectId(),
            requestDto.getReviewId(),
            requestDto.getAchievementCode(),
            requestDto.isRead(),
            requestDto.isActive(),
            false, // isDeleted siempre false al crear
            requestDto.getCreatedAt() != null ? requestDto.getCreatedAt() : LocalDateTime.now(),
            requestDto.getUpdatedAt() != null ? requestDto.getUpdatedAt() : LocalDateTime.now()
        );
    }

    /**
     * Convierte una entidad de dominio a un DTO de respuesta.
     * 
     * @param notification Entidad de dominio
     * @return DTO de respuesta
     */
    public NotificationResponseDto toResponseDto(Notification notification) {
        return new NotificationResponseDto(
            notification.getId(),
            notification.getUserId(),
            notification.getMessage().getValue(),
            notification.getNotificationType().getValue(),
            notification.getProjectId(),
            notification.getReviewId(),
            notification.getAchievementCode(),
            notification.isRead(),
            notification.isActive(),
            notification.isDeleted(),
            notification.getCreatedAt(),
            notification.getUpdatedAt()
        );
    }

    /**
     * Convierte una lista de entidades de dominio a una lista de DTOs de respuesta.
     * 
     * @param notifications Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public List<NotificationResponseDto> toResponseDtoList(List<Notification> notifications) {
        if (notifications == null) {
            return List.of();
        }
        
        return notifications.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }



    /**
     * Marca una notificación como leída.
     * 
     * @param notification Entidad de dominio
     * @return Entidad de dominio actualizada
     */
    public Notification markAsRead(Notification notification) {
        return new Notification(
            notification.getId(),
            notification.getUserId(),
            notification.getMessage(),
            notification.getNotificationType(),
            notification.getProjectId(),
            notification.getReviewId(),
            notification.getAchievementCode(),
            true, // isRead = true
            notification.isActive(),
            notification.isDeleted(),
            notification.getCreatedAt(),
            LocalDateTime.now() // updatedAt = ahora
        );
    }

    /**
     * Marca una notificación como eliminada (marcado lógico).
     * 
     * @param notification Entidad de dominio
     * @return Entidad de dominio actualizada
     */
    public Notification markAsDeleted(Notification notification) {
        return new Notification(
            notification.getId(),
            notification.getUserId(),
            notification.getMessage(),
            notification.getNotificationType(),
            notification.getProjectId(),
            notification.getReviewId(),
            notification.getAchievementCode(),
            notification.isRead(),
            notification.isActive(),
            true, // isDeleted = true
            notification.getCreatedAt(),
            LocalDateTime.now() // updatedAt = ahora
        );
    }

    /**
     * Crea una notificación de aplicación a proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Entidad de dominio
     */
    public Notification createProjectApplicationNotification(Long userId, Long projectId, String projectTitle) {
        return new Notification(
            null,
            userId,
            new NotificationMessage("Has aplicado al proyecto: " + projectTitle),
            NotificationType.PROJECT_APPLICATION,
            projectId,
            null,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de aplicación aceptada.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @return Entidad de dominio
     */
    public Notification createProjectApplicationAcceptedNotification(Long userId, Long projectId, String projectTitle) {
        return new Notification(
            null,
            userId,
            new NotificationMessage("¡Tu aplicación al proyecto '" + projectTitle + "' ha sido aceptada!"),
            NotificationType.PROJECT_APPLICATION_ACCEPTED,
            projectId,
            null,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de aplicación rechazada.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param reason Razón del rechazo (opcional)
     * @return Entidad de dominio
     */
    public Notification createProjectApplicationRejectedNotification(Long userId, Long projectId, String projectTitle, String reason) {
        String messageText = "Tu aplicación al proyecto '" + projectTitle + "' ha sido rechazada";
        if (reason != null && !reason.trim().isEmpty()) {
            messageText += ". Razón: " + reason;
        }
        
        return new Notification(
            null,
            userId,
            new NotificationMessage(messageText),
            NotificationType.PROJECT_APPLICATION_REJECTED,
            projectId,
            null,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de nuevo miembro en proyecto.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param projectTitle Título del proyecto
     * @param memberName Nombre del nuevo miembro
     * @return Entidad de dominio
     */
    public Notification createProjectMemberJoinedNotification(Long userId, Long projectId, String projectTitle, String memberName) {
        return new Notification(
            null,
            userId,
            new NotificationMessage(memberName + " se ha unido al proyecto '" + projectTitle + "'"),
            NotificationType.PROJECT_MEMBER_JOINED,
            projectId,
            null,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de review recibida.
     * 
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param reviewId ID de la review
     * @param reviewerName Nombre del revisor
     * @return Entidad de dominio
     */
    public Notification createProjectReviewReceivedNotification(Long userId, Long projectId, Long reviewId, String reviewerName) {
        return new Notification(
            null,
            userId,
            new NotificationMessage("Has recibido una nueva review de " + reviewerName),
            NotificationType.PROJECT_REVIEW_RECEIVED,
            projectId,
            reviewId,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de logro desbloqueado.
     * 
     * @param userId ID del usuario
     * @param achievementCode Código del logro
     * @param achievementName Nombre del logro
     * @return Entidad de dominio
     */
    public Notification createAchievementUnlockedNotification(Long userId, String achievementCode, String achievementName) {
        return new Notification(
            null,
            userId,
            new NotificationMessage("¡Has desbloqueado el logro: " + achievementName + "!"),
            NotificationType.ACHIEVEMENT_UNLOCKED,
            null,
            null,
            achievementCode,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Crea una notificación de bienvenida.
     * 
     * @param userId ID del usuario
     * @param username Nombre de usuario
     * @return Entidad de dominio
     */
    public Notification createWelcomeNotification(Long userId, String username) {
        return new Notification(
            null,
            userId,
            new NotificationMessage("¡Bienvenido a DevMatch, " + username + "! Estamos emocionados de tenerte con nosotros."),
            NotificationType.WELCOME_MESSAGE,
            null,
            null,
            null,
            false,
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
} 