package com.devmatch.api.user.domain.event;

import com.devmatch.api.shared.domain.model.BaseDomainEvent;

/**
 * Evento de dominio que se dispara cuando un usuario se registra exitosamente.
 * Contiene la información básica del usuario registrado.
 */
public class UserRegisteredEvent extends BaseDomainEvent {
    private final Long userId;
    private final String username;
    private final String email;

    public UserRegisteredEvent(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
