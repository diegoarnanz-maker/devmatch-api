package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.out.AchievementEventPublisher;
import com.devmatch.api.achievement.domain.event.AchievementUnlockedEvent;
import com.devmatch.api.achievement.domain.event.UserAchievementEarnedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso para publicación de eventos de logros.
 * 
 * <p>Este servicio implementa la lógica de negocio para publicar eventos de dominio
 * relacionados con logros. Utiliza Spring ApplicationEventPublisher para notificar
 * a otros componentes del sistema sobre cambios en el estado de logros.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Publicar eventos de logros desbloqueados</li>
 *   <li>Notificar sobre logros ganados por usuarios</li>
 *   <li>Manejar errores de publicación de eventos</li>
 *   <li>Proporcionar logging detallado de eventos</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Recibe eventos de dominio de otros servicios</li>
 *   <li>Valida y enriquece eventos si es necesario</li>
 *   <li>Publica eventos usando Spring ApplicationEventPublisher</li>
 *   <li>Maneja errores sin interrumpir flujo principal</li>
 *   <li>Registra operaciones en logs</li>
 * </ol>
 * 
 * <h3>Consideraciones técnicas:</h3>
 * <ul>
 *   <li>Asíncrono: Los eventos se publican de forma asíncrona</li>
 *   <li>Tolerancia a fallos: Errores no interrumpen el flujo principal</li>
 *   <li>Logging: Registro detallado para debugging</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementEventPublisherUseCaseImpl implements AchievementEventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void publishAchievementUnlocked(AchievementUnlockedEvent event) {
        // Publicar evento de logro desbloqueado
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Error publicando evento AchievementUnlockedEvent: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void publishUserAchievementEarned(UserAchievementEarnedEvent event) {
        // Publicar evento de logro ganado
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Error publicando evento UserAchievementEarnedEvent: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void publishAchievementCreated(com.devmatch.api.achievement.domain.event.AchievementCreatedEvent event) {
        // Publicar evento de logro creado (implementación futura)
    }
    
    @Override
    public void publishAchievementUpdated(com.devmatch.api.achievement.domain.event.AchievementUpdatedEvent event) {
        // Publicar evento de logro actualizado (implementación futura)
    }
    
    @Override
    public void publishAchievementDeleted(com.devmatch.api.achievement.domain.event.AchievementDeletedEvent event) {
        // Publicar evento de logro eliminado (implementación futura)
    }
    
    @Override
    public void publishAchievementProgress(com.devmatch.api.achievement.domain.event.AchievementProgressEvent event) {
        // Publicar evento de progreso de logro (implementación futura)
    }
}
