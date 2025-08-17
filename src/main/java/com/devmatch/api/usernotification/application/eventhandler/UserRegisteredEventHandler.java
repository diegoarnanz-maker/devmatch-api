package com.devmatch.api.usernotification.application.eventhandler;

// Importamos el evento que vamos a escuchar
import com.devmatch.api.user.domain.event.UserRegisteredEvent;

// Importamos el caso de uso que vamos a llamar
import com.devmatch.api.usernotification.application.port.in.NotificationManagementUseCase;

// Anotaciones de Spring y Lombok
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Manejador de eventos que escucha cuando un usuario se registra.
 * 
 * ¿Qué hace?
 * - Escucha el evento UserRegisteredEvent
 * - Crea automáticamente una notificación de bienvenida
 * - No requiere intervención manual
 */
@Slf4j  // ← Para logging automático
@Component  // ← Para que Spring lo detecte como bean
@RequiredArgsConstructor  // ← Para inyección automática de dependencias
public class UserRegisteredEventHandler {
    
    // Inyectamos el caso de uso que crea notificaciones
    private final NotificationManagementUseCase notificationManagementUseCase;
    
    /**
     * Método que se ejecuta AUTOMÁTICAMENTE cuando llega un UserRegisteredEvent.
     * 
     * @EventListener ← Esta anotación hace que Spring escuche el evento
     * @param event El evento que contiene la información del usuario registrado
     */
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        // Log para debugging - vemos que llegó el evento
        log.info("Usuario registrado: {}, creando notificación de bienvenida", event.getUserId());
        
        try {
            // Llamamos al caso de uso para crear la notificación
            // Esto es lo que antes se hacía manualmente con POST /notifications/internal/welcome/{userId}
            notificationManagementUseCase.createWelcomeNotification(event.getUserId());
            
            // Log de éxito
            log.info("Notificación de bienvenida creada exitosamente para usuario: {}", event.getUserId());
            
        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al crear notificación de bienvenida para usuario: {}", event.getUserId(), e);
        }
    }
}
