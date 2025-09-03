package com.devmatch.api.projectmessage.domain.model.valueobject;

/**
 * Enum que representa los tipos de mensaje disponibles en el sistema.
 * Define diferentes categorías de mensajes para facilitar la organización y filtrado.
 */
public enum MessageType {
    TEXT("Mensaje de texto normal"),
    ANNOUNCEMENT("Anuncio del proyecto"),
    TASK_UPDATE("Actualización de tarea"),
    MEETING_REMINDER("Recordatorio de reunión"),
    FILE_SHARE("Compartir archivo"),
    CODE_REVIEW("Revisión de código"),
    SYSTEM("Mensaje del sistema"),
    GENERAL("Mensaje general");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return this.name();
    }

    public static MessageType fromValue(String value) {
        for (MessageType type : MessageType.values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de mensaje no válido: " + value);
    }
}