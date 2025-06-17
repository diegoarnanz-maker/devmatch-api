package com.devmatch.api.user.domain.model.valueobject;

import java.util.Objects;

public class Password {
    private final String value;

    public Password(String value) {
        validatePassword(value);
        this.value = value;
    }

    private void validatePassword(String value) {
        if (value == null || value.length() < 8 || value.length() > 255) {
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 255 caracteres");
        }
        if (!value.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una mayúscula, una minúscula, un número y un símbolo especial");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "********"; // Por seguridad, no mostramos la contraseña
    }
} 