package com.devmatch.api.projectmessage.domain.model.valueobject;

import lombok.Getter;

/**
 * Value Object para el tipo de mensaje del proyecto.
 * Define los tipos de mensajes permitidos en el sistema.
 */
@Getter
public enum MessageType {
    
    TEXT("TEXT", "Mensaje de texto"),
    SYSTEM("SYSTEM", "Mensaje del sistema"),
    ANNOUNCEMENT("ANNOUNCEMENT", "Anuncio"),
    TASK_UPDATE("TASK_UPDATE", "Actualización de tarea"),
    MEETING_REMINDER("MEETING_REMINDER", "Recordatorio de reunión"),
    FILE_SHARE("FILE_SHARE", "Compartir archivo"),
    CODE_REVIEW("CODE_REVIEW", "Revisión de código"),
    GENERAL("GENERAL", "Mensaje general");
    
    private final String value;
    private final String description;
    
    MessageType(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public static MessageType fromValue(String value) {
        for (MessageType type : MessageType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de mensaje no válido: " + value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
