package com.devmatch.api.projectmessage.application.service;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import com.devmatch.api.projectmessage.application.mapper.ProjectMessageMapper;
import com.devmatch.api.projectmessage.application.port.in.AdminProjectMessageUseCase;
import com.devmatch.api.projectmessage.application.port.out.ProjectMessageRepository;
import com.devmatch.api.projectmessage.application.port.out.UserService;
import com.devmatch.api.projectmessage.domain.exception.ProjectMessageNotFoundException;
import com.devmatch.api.projectmessage.domain.model.ProjectMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para la gestión administrativa de mensajes.
 * Permite a los administradores gestionar todos los mensajes del sistema.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminProjectMessageService implements AdminProjectMessageUseCase {
    
    private final ProjectMessageRepository projectMessageRepository;
    private final ProjectMessageMapper projectMessageMapper;
    private final UserService userService;
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> getAllMessages(Pageable pageable) {
        log.info("Obteniendo todos los mensajes del sistema - página {}", pageable.getPageNumber());
        
        Page<ProjectMessage> messages = projectMessageRepository.findByCriteria(
            null, null, null, null, null, true, pageable
        );
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> getProjectMessages(Long projectId, Pageable pageable) {
        log.info("Obteniendo mensajes del proyecto {} - página {}", projectId, pageable.getPageNumber());
        
        Page<ProjectMessage> messages = projectMessageRepository.findByProjectId(projectId, pageable);
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> getUserMessages(Long userId, Pageable pageable) {
        log.info("Obteniendo mensajes del usuario {} - página {}", userId, pageable.getPageNumber());
        
        Page<ProjectMessage> messages = projectMessageRepository.findBySenderId(userId, pageable);
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMessageResponseDto> searchMessages(ProjectMessageSearchRequestDto searchRequest, Pageable pageable) {
        log.info("Búsqueda administrativa de mensajes con criterios");
        
        Page<ProjectMessage> messages = projectMessageRepository.findByCriteria(
            searchRequest.getProjectId(),
            searchRequest.getSenderId(),
            searchRequest.getMessageType(),
            searchRequest.getFromDate(),
            searchRequest.getToDate(),
            searchRequest.getIncludeDeleted() != null ? searchRequest.getIncludeDeleted() : true,
            pageable
        );
        
        return messages.map(projectMessageMapper::toResponseDto);
    }
    
    @Override
    public void deleteMessage(Long messageId) {
        log.info("Eliminación administrativa del mensaje {}", messageId);
        
        // Verificar que el mensaje existe
        if (!projectMessageRepository.existsById(messageId)) {
            throw new ProjectMessageNotFoundException("Mensaje no encontrado: " + messageId);
        }
        
        // Eliminación física (solo administradores)
        projectMessageRepository.deleteById(messageId);
        
        log.info("Mensaje {} eliminado físicamente por administrador", messageId);
    }
    
    @Override
    public ProjectMessageResponseDto restoreMessage(Long messageId) {
        log.info("Restaurando mensaje {} por administrador", messageId);
        
        ProjectMessage message = projectMessageRepository.findById(messageId)
            .orElseThrow(() -> new ProjectMessageNotFoundException("Mensaje no encontrado: " + messageId));
        
        if (!message.isDeleted()) {
            log.warn("El mensaje {} no está eliminado, no se puede restaurar", messageId);
            return projectMessageMapper.toResponseDto(message);
        }
        
        // Restaurar mensaje (crear nueva instancia sin soft delete)
        ProjectMessage restoredMessage = new ProjectMessage(
            message.getId(),
            message.getProjectId(),
            message.getSenderId(),
            message.getContent(),
            message.getType(),
            message.getReplyToMessageId(),
            message.getSentAt(),
            message.getCreatedAt(),
            LocalDateTime.now(), // updatedAt
            false // isDeleted = false
        );
        
        ProjectMessage savedMessage = projectMessageRepository.save(restoredMessage);
        
        log.info("Mensaje {} restaurado exitosamente", messageId);
        
        return projectMessageMapper.toResponseDto(savedMessage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectMessageStatsDto getMessageStats() {
        log.info("Obteniendo estadísticas de mensajes del sistema");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusWeeks(1);
        LocalDateTime monthStart = now.minusMonths(1);
        
        // Contar mensajes por diferentes criterios
        long totalMessages = projectMessageRepository.countByCriteria(null, null, null, null, null, true);
        long activeMessages = projectMessageRepository.countByCriteria(null, null, null, null, null, false);
        long deletedMessages = totalMessages - activeMessages;
        long messagesToday = projectMessageRepository.countByCriteria(null, null, null, todayStart, null, false);
        long messagesThisWeek = projectMessageRepository.countByCriteria(null, null, null, weekStart, null, false);
        long messagesThisMonth = projectMessageRepository.countByCriteria(null, null, null, monthStart, null, false);
        
        return new ProjectMessageStatsDtoImpl(
            totalMessages,
            activeMessages,
            deletedMessages,
            messagesToday,
            messagesThisWeek,
            messagesThisMonth
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectMessageResponseDto> getMostActiveMessages(int limit) {
        log.info("Obteniendo {} mensajes más activos", limit);
        
        // Esta implementación es simplificada. En un caso real, necesitarías
        // una consulta más compleja que cuente las respuestas por mensaje
        Page<ProjectMessage> messages = projectMessageRepository.findByCriteria(
            null, null, null, null, null, false, 
            Pageable.ofSize(limit)
        );
        
        return messages.getContent().stream()
            .map(projectMessageMapper::toResponseDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserMessageStatsDto> getMostActiveUsers(int limit) {
        log.info("Obteniendo {} usuarios más activos", limit);
        
        // Esta implementación es simplificada. En un caso real, necesitarías
        // una consulta que agrupe por senderId y cuente mensajes
        Page<ProjectMessage> messages = projectMessageRepository.findByCriteria(
            null, null, null, null, null, false,
            Pageable.ofSize(limit * 10) // Obtener más para poder agrupar
        );
        
        return messages.getContent().stream()
            .collect(Collectors.groupingBy(ProjectMessage::getSenderId))
            .entrySet().stream()
            .map(entry -> {
                Long userId = entry.getKey();
                List<ProjectMessage> userMessages = entry.getValue();
                String username = userService.getUsernameById(userId);
                
                long totalMessages = userMessages.size();
                long messagesThisMonth = userMessages.stream()
                    .filter(msg -> msg.getSentAt().isAfter(LocalDateTime.now().minusMonths(1)))
                    .count();
                
                return new UserMessageStatsDtoImpl(userId, username, totalMessages, messagesThisMonth);
            })
            .sorted((a, b) -> Long.compare(b.getTotalMessages(), a.getTotalMessages()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    // Implementaciones de DTOs internos
    
    private static class ProjectMessageStatsDtoImpl implements ProjectMessageStatsDto {
        private final long totalMessages;
        private final long totalActiveMessages;
        private final long totalDeletedMessages;
        private final long messagesToday;
        private final long messagesThisWeek;
        private final long messagesThisMonth;
        
        public ProjectMessageStatsDtoImpl(long totalMessages, long totalActiveMessages, 
                                        long totalDeletedMessages, long messagesToday, 
                                        long messagesThisWeek, long messagesThisMonth) {
            this.totalMessages = totalMessages;
            this.totalActiveMessages = totalActiveMessages;
            this.totalDeletedMessages = totalDeletedMessages;
            this.messagesToday = messagesToday;
            this.messagesThisWeek = messagesThisWeek;
            this.messagesThisMonth = messagesThisMonth;
        }
        
        @Override
        public long getTotalMessages() { return totalMessages; }
        
        @Override
        public long getTotalActiveMessages() { return totalActiveMessages; }
        
        @Override
        public long getTotalDeletedMessages() { return totalDeletedMessages; }
        
        @Override
        public long getMessagesToday() { return messagesToday; }
        
        @Override
        public long getMessagesThisWeek() { return messagesThisWeek; }
        
        @Override
        public long getMessagesThisMonth() { return messagesThisMonth; }
    }
    
    private static class UserMessageStatsDtoImpl implements UserMessageStatsDto {
        private final Long userId;
        private final String username;
        private final long totalMessages;
        private final long messagesThisMonth;
        
        public UserMessageStatsDtoImpl(Long userId, String username, long totalMessages, long messagesThisMonth) {
            this.userId = userId;
            this.username = username;
            this.totalMessages = totalMessages;
            this.messagesThisMonth = messagesThisMonth;
        }
        
        @Override
        public Long getUserId() { return userId; }
        
        @Override
        public String getUsername() { return username; }
        
        @Override
        public long getTotalMessages() { return totalMessages; }
        
        @Override
        public long getMessagesThisMonth() { return messagesThisMonth; }
    }
}
