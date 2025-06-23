package com.devmatch.api.role.domain.model;

import lombok.Getter;

@Getter
public class Role {
    private final Long id;
    private final String name;
    private final String description;

    public Role(String name, String description) {
        this.id = null; // Se asignará al persistir
        this.name = name;
        this.description = description;
    }

    public Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
} 