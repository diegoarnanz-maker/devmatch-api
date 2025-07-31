package com.devmatch.api.usernotification.domain.model.valueobject;

public class NotificationMessage {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 500;
    
    private final String value;

    public NotificationMessage(String value) {
        validateMessage(value);
        this.value = value;
    }

    private void validateMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("El mensaje de notificación no puede ser null");
        }
        
        if (message.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje de notificación no puede estar vacío");
        }
        
        if (message.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("El mensaje de notificación debe tener al menos " + MIN_LENGTH + " caracteres");
        }
        
        if (message.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El mensaje de notificación no puede exceder " + MAX_LENGTH + " caracteres");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationMessage that = (NotificationMessage) obj;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 