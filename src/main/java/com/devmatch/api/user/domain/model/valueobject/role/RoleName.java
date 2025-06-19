package com.devmatch.api.user.domain.model.valueobject.role;

public class RoleName {
    private final String value;
    private static final String[] ALLOWED_ROLES = {"USER", "ADMIN"};

    public RoleName(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }
        if (value.length() > 30) {
            throw new IllegalArgumentException("El nombre del rol no puede tener más de 30 caracteres");
        }
        boolean isValid = false;
        for (String role : ALLOWED_ROLES) {
            if (role.equals(value)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Rol no válido. Los roles permitidos son: " + String.join(", ", ALLOWED_ROLES));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleName roleName = (RoleName) o;
        return value.equals(roleName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 