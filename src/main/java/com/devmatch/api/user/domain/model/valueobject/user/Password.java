package com.devmatch.api.user.domain.model.valueobject.user;

public class Password {
    private final String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!value.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una mayúscula");
        }
        if (!value.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una minúscula");
        }
        if (!value.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 