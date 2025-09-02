package com.devmatch.api.projectmessage.domain.model.valueobject;

import lombok.Getter;

/**
 * Value Object para el estado de un mensaje del proyecto.
 * Define los estados posibles de un mensaje en el sistema.
 */
@Getter
public enum MessageStatus {
    
    SENT("SENT", "Enviado"),
    DELIVERED("DELIVERED", "Entregado"),
    READ("READ", "Leído"),
    EDITED("EDITED", "Editado"),
    DELETED("DELETED", "Eliminado"),
    ARCHIVED("ARCHIVED", "Archivado");
    
    private final String value;
    private final String description;
    
    MessageStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public static MessageStatus fromValue(String value) {
        for (MessageStatus status : MessageStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de mensaje no válido: " + value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
