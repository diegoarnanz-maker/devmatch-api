package com.devmatch.api.usernotification.application.service;

import com.devmatch.api.usernotification.application.dto.NotificationResponseDto;
import com.devmatch.api.usernotification.application.mapper.NotificationMapper;
import com.devmatch.api.usernotification.application.port.in.NotificationQueryUseCase;
import com.devmatch.api.usernotification.application.port.out.NotificationRepositoryPort;
import com.devmatch.api.usernotification.domain.exception.NotificationNotFoundException;
import com.devmatch.api.usernotification.domain.exception.NotificationUserMismatchException;
import com.devmatch.api.usernotification.domain.model.Notification;
import com.devmatch.api.usernotification.domain.model.valueobject.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del caso de uso de consulta de notificaciones.
 * Orquesta las operaciones de búsqueda y consulta de notificaciones.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryUseCaseImpl implements NotificationQueryUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationMapper notificationMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Optional<NotificationResponseDto> getNotificationById(Long notificationId, Long userId) {
        Optional<Notification> notification = notificationRepositoryPort.findByIdAndUserId(notificationId, userId);
        return notification.map(notificationMapper::toResponseDto);
    }

    @Override
    public List<NotificationResponseDto> getAllUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepositoryPort.findByUserId(userId);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsPaginated(Long userId, int page, int size) {
        List<Notification> notifications = notificationRepositoryPort.findByUserIdPaginated(userId, page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUnreadUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepositoryPort.findUnreadByUserId(userId);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUnreadUserNotificationsPaginated(Long userId, int page, int size) {
        List<Notification> notifications = notificationRepositoryPort.findUnreadByUserIdPaginated(userId, page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByType(Long userId, String notificationType) {
        try {
            NotificationType type = NotificationType.fromString(notificationType);
            List<Notification> notifications = notificationRepositoryPort.findByUserIdAndType(userId, type);
            return notificationMapper.toResponseDtoList(notifications);
        } catch (IllegalArgumentException e) {
            // Si el tipo no es válido, retornar lista vacía
            return List.of();
        }
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByTypePaginated(Long userId, String notificationType, int page, int size) {
        try {
            NotificationType type = NotificationType.fromString(notificationType);
            List<Notification> notifications = notificationRepositoryPort.findByUserIdAndTypePaginated(userId, type, page, size);
            return notificationMapper.toResponseDtoList(notifications);
        } catch (IllegalArgumentException e) {
            // Si el tipo no es válido, retornar lista vacía
            return List.of();
        }
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByProject(Long userId, Long projectId) {
        List<Notification> notifications = notificationRepositoryPort.findByUserIdAndProjectId(userId, projectId);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByProjectPaginated(Long userId, Long projectId, int page, int size) {
        List<Notification> notifications = notificationRepositoryPort.findByUserIdAndProjectIdPaginated(userId, projectId, page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByReview(Long userId, Long reviewId) {
        List<Notification> notifications = notificationRepositoryPort.findByUserIdAndReviewId(userId, reviewId);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByReviewPaginated(Long userId, Long reviewId, int page, int size) {
        List<Notification> notifications = notificationRepositoryPort.findByUserIdAndReviewIdPaginated(userId, reviewId, page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserAchievementNotifications(Long userId) {
        List<Notification> notifications = notificationRepositoryPort.findAchievementNotificationsByUserId(userId);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserAchievementNotificationsPaginated(Long userId, int page, int size) {
        List<Notification> notifications = notificationRepositoryPort.findAchievementNotificationsByUserIdPaginated(userId, page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByDateRange(Long userId, String startDate, String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
            List<Notification> notifications = notificationRepositoryPort.findByUserIdAndDateRange(userId, start, end);
            return notificationMapper.toResponseDtoList(notifications);
        } catch (DateTimeParseException e) {
            // Si el formato de fecha no es válido, retornar lista vacía
            return List.of();
        }
    }

    @Override
    public List<NotificationResponseDto> getUserNotificationsByDateRangePaginated(Long userId, String startDate, String endDate, int page, int size) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
            List<Notification> notifications = notificationRepositoryPort.findByUserIdAndDateRangePaginated(userId, start, end, page, size);
            return notificationMapper.toResponseDtoList(notifications);
        } catch (DateTimeParseException e) {
            // Si el formato de fecha no es válido, retornar lista vacía
            return List.of();
        }
    }

    @Override
    public long countUserNotifications(Long userId) {
        return notificationRepositoryPort.countByUserId(userId);
    }

    @Override
    public long countUnreadUserNotifications(Long userId) {
        return notificationRepositoryPort.countUnreadByUserId(userId);
    }

    @Override
    public long countUserNotificationsByType(Long userId, String notificationType) {
        try {
            NotificationType type = NotificationType.fromString(notificationType);
            return notificationRepositoryPort.countByUserIdAndType(userId, type);
        } catch (IllegalArgumentException e) {
            // Si el tipo no es válido, retornar 0
            return 0;
        }
    }

    @Override
    public List<NotificationResponseDto> searchUserNotifications(Long userId, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return List.of();
        }
        
        List<Notification> notifications = notificationRepositoryPort.searchByUserIdAndMessageContaining(userId, searchText.trim());
        return notificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public List<NotificationResponseDto> searchUserNotificationsPaginated(Long userId, String searchText, int page, int size) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return List.of();
        }
        
        List<Notification> notifications = notificationRepositoryPort.searchByUserIdAndMessageContainingPaginated(userId, searchText.trim(), page, size);
        return notificationMapper.toResponseDtoList(notifications);
    }
} 