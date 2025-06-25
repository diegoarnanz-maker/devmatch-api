package com.devmatch.api.project.domain.model.valueobject;

/**
 * Estados posibles de un proyecto en la plataforma DevMatch
 */
public enum ProjectStatus {
    
    /**
     * Proyecto abierto para recibir aplicaciones de desarrolladores
     */
    OPEN("Abierto"),
    
    /**
     * Proyecto en desarrollo activo
     */
    IN_PROGRESS("En Progreso"),
    
    /**
     * Proyecto completado exitosamente
     */
    COMPLETED("Completado"),
    
    /**
     * Proyecto cancelado o suspendido
     */
    CANCELLED("Cancelado"),
    
    /**
     * Proyecto en revisión o evaluación
     */
    UNDER_REVIEW("En Revisión");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 