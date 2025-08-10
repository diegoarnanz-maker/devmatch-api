package com.devmatch.api.usernotification.application.service;

import com.devmatch.api.usernotification.application.dto.NotificationRequestDto;
import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.dto.NotificationStatusRequestDto;
import com.devmatch.api.usernotification.application.mapper.NotificationMapper;
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;
import com.devmatch.api.usernotification.application.port.out.NotificationEventPublisherPort;
import com.devmatch.api.usernotification.application.port.out.NotificationRepositoryPort;
import com.devmatch.api.usernotification.application.port.out.ProjectQueryPort;
import com.devmatch.api.usernotification.domain.exception.NotificationDuplicateException;
import com.devmatch.api.usernotification.domain.exception.NotificationInvalidStateException;
import com.devmatch.api.usernotification.domain.exception.NotificationLimitExceededException;
import com.devmatch.api.usernotification.domain.exception.NotificationNotFoundException;
import com.devmatch.api.usernotification.domain.exception.NotificationUserMismatchException;
import com.devmatch.api.usernotification.domain.model.Notification;
import com.devmatch.api.usernotification.domain.service.NotificationDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso de gestión de notificaciones.
 * Orquesta las operaciones de creación, actualización y eliminación de notificaciones.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationManagementUseCaseImpl implements NotificationManagementUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationEventPublisherPort notificationEventPublisherPort;
    private final NotificationMapper notificationMapper;
    private final NotificationDomainService notificationDomainService;
    private final ProjectQueryPort projectQueryPort;

    private static final int MAX_NOTIFICATIONS_PER_USER = 1000;
    private static final int DUPLICATE_TIME_WINDOW_MINUTES = 5;

    @Override
    @Transactional
    public NotificationResponseDto createNotification(NotificationRequestDto request) {
        // Validar límite de notificaciones
        List<Notification> existingNotifications = notificationRepositoryPort.findByUserId(request.getUserId());
        if (!notificationDomainService.canCreateNotification(request.getUserId(), existingNotifications, MAX_NOTIFICATIONS_PER_USER)) {
            throw new NotificationLimitExceededException(request.getUserId(), existingNotifications.size(), MAX_NOTIFICATIONS_PER_USER);
        }

        // Verificar duplicados recientes
        if (notificationDomainService.hasRecentDuplicate(request.getUserId(), request.getNotificationType(), 
                request.getProjectId(), existingNotifications, DUPLICATE_TIME_WINDOW_MINUTES)) {
            throw new NotificationDuplicateException(request.getUserId(), request.getNotificationType(), request.getProjectId());
        }

        // Crear notificación
        Notification notification = notificationMapper.toDomain(request);
        Notification savedNotification = notificationRepositoryPort.save(notification);

        // Publicar evento
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);

        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createProjectApplicationNotification(Long userId, Long projectId) {
        // Obtener el título del proyecto automáticamente
        String projectTitle = projectQueryPort.getProjectTitleById(projectId)
                .orElse("Proyecto #" + projectId);
        
        Notification notification = notificationMapper.createProjectApplicationNotification(userId, projectId, projectTitle);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createProjectApplicationAcceptedNotification(Long userId, Long projectId) {
        // Obtener el título del proyecto automáticamente
        String projectTitle = projectQueryPort.getProjectTitleById(projectId)
                .orElse("Proyecto #" + projectId);
        
        Notification notification = notificationDomainService.createProjectApplicationAcceptedNotification(userId, projectId, projectTitle);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createProjectApplicationRejectedNotification(Long userId, Long projectId, String reason) {
        // Obtener el título del proyecto automáticamente
        String projectTitle = projectQueryPort.getProjectTitleById(projectId)
                .orElse("Proyecto #" + projectId);
        
        Notification notification = notificationMapper.createProjectApplicationRejectedNotification(userId, projectId, projectTitle, reason);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createProjectMemberJoinedNotification(Long userId, Long projectId, String memberName) {
        // Obtener el título del proyecto automáticamente
        String projectTitle = projectQueryPort.getProjectTitleById(projectId)
                .orElse("Proyecto #" + projectId);
        
        Notification notification = notificationMapper.createProjectMemberJoinedNotification(userId, projectId, projectTitle, memberName);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createProjectReviewReceivedNotification(Long userId, Long projectId, Long reviewId, String reviewerName) {
        Notification notification = notificationMapper.createProjectReviewReceivedNotification(userId, projectId, reviewId, reviewerName);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createAchievementUnlockedNotification(Long userId, String achievementCode, String achievementName) {
        Notification notification = notificationMapper.createAchievementUnlockedNotification(userId, achievementCode, achievementName);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createWelcomeNotification(Long userId) {
        // TODO: Obtener username del usuario desde la base de datos
        // Por ahora usamos un placeholder, pero deberías implementar la consulta al usuario
        String username = "Usuario #" + userId; // Placeholder temporal
        
        Notification notification = notificationMapper.createWelcomeNotification(userId, username);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto createSystemNotification(Long userId, String message) {
        Notification notification = notificationMapper.createSystemNotification(userId, message);
        Notification savedNotification = notificationRepositoryPort.save(notification);
        notificationEventPublisherPort.publishNotificationCreated(savedNotification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDto markAsRead(Long notificationId, Long userId) {
        // Buscar notificación y validar propiedad
        Notification notification = notificationRepositoryPort.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId, userId));

        // Validar estado
        if (!notificationDomainService.canMarkAsRead(notification)) {
            throw NotificationInvalidStateException.alreadyRead(notificationId);
        }

        // Marcar como leída
        Notification updatedNotification = notificationMapper.markAsRead(notification);
        Notification savedNotification = notificationRepositoryPort.save(updatedNotification);

        // Publicar evento
        notificationEventPublisherPort.publishNotificationRead(savedNotification);

        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public List<NotificationResponseDto> markMultipleAsRead(List<Long> notificationIds, Long userId) {
        List<NotificationResponseDto> updatedNotifications = new java.util.ArrayList<>();

        for (Long notificationId : notificationIds) {
            try {
                NotificationResponseDto updated = markAsRead(notificationId, userId);
                updatedNotifications.add(updated);
            } catch (Exception e) {
                // Continuar con las siguientes notificaciones si una falla
                System.err.println("Error marcando notificación " + notificationId + " como leída: " + e.getMessage());
            }
        }

        // Publicar evento masivo
        if (!updatedNotifications.isEmpty()) {
            notificationEventPublisherPort.publishMultipleNotificationsRead(userId, updatedNotifications.size());
        }

        return updatedNotifications;
    }

    @Override
    @Transactional
    public int markAllAsRead(Long userId) {
        int updatedCount = notificationRepositoryPort.markAllAsRead(userId);
        
        if (updatedCount > 0) {
            notificationEventPublisherPort.publishAllNotificationsRead(userId, updatedCount);
        }

        return updatedCount;
    }

    @Override
    @Transactional
    public NotificationResponseDto updateNotificationStatus(Long notificationId, Long userId, NotificationStatusRequestDto statusRequest) {
        // Buscar notificación y validar propiedad
        Notification notification = notificationRepositoryPort.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId, userId));

        // Validar que hay cambios para actualizar
        if (!statusRequest.hasUpdates()) {
            return notificationMapper.toResponseDto(notification);
        }

        // Actualizar notificación
        Notification updatedNotification = notificationMapper.updateFromStatusDto(notification, statusRequest);
        Notification savedNotification = notificationRepositoryPort.save(updatedNotification);

        // Publicar evento
        notificationEventPublisherPort.publishNotificationUpdated(savedNotification);

        return notificationMapper.toResponseDto(savedNotification);
    }

    @Override
    @Transactional
    public boolean deleteNotification(Long notificationId, Long userId) {
        // Buscar notificación y validar propiedad
        Notification notification = notificationRepositoryPort.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId, userId));

        // Validar estado
        if (!notificationDomainService.canDeleteNotification(notification)) {
            throw NotificationInvalidStateException.alreadyDeleted(notificationId);
        }

        // Eliminar notificación
        boolean deleted = notificationRepositoryPort.deleteByIdAndUserId(notificationId, userId);

        if (deleted) {
            notificationEventPublisherPort.publishNotificationDeleted(notification);
        }

        return deleted;
    }

    @Override
    @Transactional
    public int deleteMultipleNotifications(List<Long> notificationIds, Long userId) {
        int deletedCount = notificationRepositoryPort.deleteMultipleByIdsAndUserId(notificationIds, userId);
        return deletedCount;
    }

    @Override
    @Transactional
    public int deleteAllUserNotifications(Long userId) {
        int deletedCount = notificationRepositoryPort.deleteAllByUserId(userId);
        return deletedCount;
    }
} 