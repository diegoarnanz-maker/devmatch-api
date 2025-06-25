package com.devmatch.api.project.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un proyecto
 */
public class ProjectNotFoundException extends RuntimeException {
    
    public ProjectNotFoundException(Long id) {
        super("No se encontró el proyecto con ID: " + id);
    }
    
    public ProjectNotFoundException(String title) {
        super("No se encontró el proyecto con título: " + title);
    }
} 