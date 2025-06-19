package com.devmatch.api.user.domain.model;

import com.devmatch.api.user.domain.model.valueobject.role.RoleName;
import lombok.Getter;

@Getter
public class Role {
    private final Long id;
    private final RoleName name;
    private final String description;

    public Role(RoleName name, String description) {
        this.id = null; // Se asignará al persistir
        this.name = name;
        this.description = description;
    }

    public Role(Long id, RoleName name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
