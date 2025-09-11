package com.devmatch.api.achievement.application.eventhandler;

import com.devmatch.api.user.domain.event.UserRegisteredEvent;
import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Manejador de eventos de registro de usuario.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementUserRegisteredEventHandler {
    
    private final AchievementTriggerUseCase achievementTriggerUseCase;
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un UserRegisteredEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información del usuario registrado
     */
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Usuario registrado: {}, creando logro FIRST_LOGIN", event.getUserId());
        
        try {
            // Crear automáticamente el logro FIRST_LOGIN para el usuario
            AchievementTriggerRequestDto triggerRequest = new AchievementTriggerRequestDto(
                event.getUserId(),
                "USER_REGISTRATION"
            );
            
            achievementTriggerUseCase.processAchievementTrigger(triggerRequest);
            
            log.info("Logro FIRST_LOGIN creado exitosamente para usuario: {}", event.getUserId());
            
        } catch (Exception e) {
            log.error("Error al crear logro FIRST_LOGIN para usuario: {}", event.getUserId(), e);
        }
    }
}
