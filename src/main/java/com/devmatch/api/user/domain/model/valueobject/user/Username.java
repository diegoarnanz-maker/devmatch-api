package com.devmatch.api.user.domain.model.valueobject.user;

public class Username {
    private final String value;

    public Username(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 3 caracteres");
        }
        if (value.length() > 30) {
            throw new IllegalArgumentException("El nombre de usuario no puede tener más de 30 caracteres");
        }
        if (!value.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("El nombre de usuario solo puede contener letras, números, guiones y guiones bajos");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return value.equals(username.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 