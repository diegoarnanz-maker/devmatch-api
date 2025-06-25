package com.devmatch.api.project.domain.service;

import org.springframework.stereotype.Service;

import com.devmatch.api.project.domain.exception.ProjectLimitExceededException;

/**
 * Servicio de dominio que encapsula reglas de negocio y validaciones
 * que no pertenecen naturalmente a la entidad Project.
 */
@Service
public class ProjectDomainService {
    
    // Constante para el límite máximo de proyectos por usuario
    private static final int MAX_PROJECTS_PER_USER = 5;
    
    /**
     * Valida si un usuario puede crear un nuevo proyecto
     * @param ownerId ID del usuario propietario
     * @param currentProjectCount Número actual de proyectos del usuario
     * @throws ProjectLimitExceededException si el usuario ha alcanzado el límite
     */
    public void validateProjectCreation(Long ownerId, long currentProjectCount) {
        if (currentProjectCount >= MAX_PROJECTS_PER_USER) {
            throw new ProjectLimitExceededException(ownerId, (int) currentProjectCount, MAX_PROJECTS_PER_USER);
        }
    }
    
    /**
     * Obtiene el límite máximo de proyectos por usuario
     * @return Límite máximo de proyectos
     */
    public int getMaxProjectsPerUser() {
        return MAX_PROJECTS_PER_USER;
    }
} 