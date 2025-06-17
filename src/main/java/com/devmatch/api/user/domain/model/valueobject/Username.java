package com.devmatch.api.user.domain.model.valueobject;

import java.util.Objects;

public class Username {
    private final String value;

    public Username(String value) {
        validateUsername(value);
        this.value = value;
    }

    private void validateUsername(String value) {
        if (value == null || value.length() < 3 || value.length() > 50) {
            throw new IllegalArgumentException("Username debe tener entre 3 y 50 caracteres");
        }
        if (!value.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("Username solo puede contener letras, números, guiones y guiones bajos");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
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