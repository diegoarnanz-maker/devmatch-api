package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.out.AchievementEventPublisher;
import com.devmatch.api.achievement.domain.event.AchievementUnlockedEvent;
import com.devmatch.api.achievement.domain.event.UserAchievementEarnedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso del publicador de eventos de achievements.
 * Utiliza Spring ApplicationEventPublisher para publicar eventos de dominio.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementEventPublisherUseCaseImpl implements AchievementEventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void publishAchievementUnlocked(AchievementUnlockedEvent event) {
        log.info("Publicando evento AchievementUnlockedEvent: {}", event);
        
        try {
            applicationEventPublisher.publishEvent(event);
            log.debug("Evento AchievementUnlockedEvent publicado exitosamente");
        } catch (Exception e) {
            log.error("Error publicando evento AchievementUnlockedEvent: {}", e.getMessage(), e);
            // No lanzamos la excepción para no interrumpir el flujo principal
        }
    }
    
    @Override
    public void publishUserAchievementEarned(UserAchievementEarnedEvent event) {
        log.info("Publicando evento UserAchievementEarnedEvent: {}", event);
        
        try {
            applicationEventPublisher.publishEvent(event);
            log.debug("Evento UserAchievementEarnedEvent publicado exitosamente");
        } catch (Exception e) {
            log.error("Error publicando evento UserAchievementEarnedEvent: {}", e.getMessage(), e);
            // No lanzamos la excepción para no interrumpir el flujo principal
        }
    }
    
    // Métodos temporales para otros eventos (por ahora solo loguean)
    @Override
    public void publishAchievementCreated(com.devmatch.api.achievement.domain.event.AchievementCreatedEvent event) {
        log.info("Evento AchievementCreatedEvent recibido (no implementado): {}", event);
    }
    
    @Override
    public void publishAchievementUpdated(com.devmatch.api.achievement.domain.event.AchievementUpdatedEvent event) {
        log.info("Evento AchievementUpdatedEvent recibido (no implementado): {}", event);
    }
    
    @Override
    public void publishAchievementDeleted(com.devmatch.api.achievement.domain.event.AchievementDeletedEvent event) {
        log.info("Evento AchievementDeletedEvent recibido (no implementado): {}", event);
    }
    
    @Override
    public void publishAchievementProgress(com.devmatch.api.achievement.domain.event.AchievementProgressEvent event) {
        log.info("Evento AchievementProgressEvent recibido (no implementado): {}", event);
    }
}
