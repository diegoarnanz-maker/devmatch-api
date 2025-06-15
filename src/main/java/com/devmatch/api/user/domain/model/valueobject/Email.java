package com.devmatch.api.user.domain.model.valueobject;

import java.util.Objects;

public class Email {
    private final String value;

    public Email(String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(String value) {
        if (value == null || value.length() > 100) {
            throw new IllegalArgumentException("Email no puede ser nulo y no debe exceder los 100 caracteres");
        }
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email debe tener un formato válido");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 