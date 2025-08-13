package com.devmatch.api.shared.infrastructure.event;

import com.devmatch.api.shared.application.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adaptador de infraestructura que implementa DomainEventPublisher usando Spring Events.
 * Convierte la publicación de eventos de dominio en eventos de Spring.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    public void publish(Object event) {
        log.debug("Publicando evento de dominio: {}", event.getClass().getSimpleName());
        eventPublisher.publishEvent(event);
    }
}
