package com.devmatch.api.shared.domain.model;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Clase base para todos los eventos de dominio.
 * Proporciona funcionalidad común como timestamp de ocurrencia.
 */
@Getter
public abstract class BaseDomainEvent {
    private final LocalDateTime occurredOn;
    
    protected BaseDomainEvent() {
        this.occurredOn = LocalDateTime.now();
    }
}
