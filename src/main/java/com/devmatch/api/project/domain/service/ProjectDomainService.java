package com.devmatch.api.project.domain.service;

import org.springframework.stereotype.Service;

import com.devmatch.api.project.domain.exception.ProjectLimitExceededException;

/**
 * Servicio de dominio que encapsula reglas de negocio y validaciones
 * relacionadas con proyectos que no pertenecen naturalmente a las entidades.
 * 
 * <p>Este servicio contiene la lógica de negocio compleja que involucra
 * múltiples entidades o que requiere validaciones que van más allá de
 * las responsabilidades de una sola entidad.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li><strong>Validación de límites:</strong> Verificar restricciones de cantidad de proyectos</li>
 *   <li><strong>Reglas de negocio:</strong> Aplicar lógica compleja del dominio</li>
 *   <li><strong>Validaciones cruzadas:</strong> Verificar reglas entre múltiples entidades</li>
 *   <li><strong>Políticas de negocio:</strong> Implementar reglas específicas del dominio</li>
 * </ul>
 * 
 * <h3>Métodos principales:</h3>
 * <ul>
 *   <li><strong>canUserCreateProject:</strong> Valida si un usuario puede crear un proyecto</li>
 *   <li><strong>canUserJoinProject:</strong> Valida si un usuario puede unirse a un proyecto</li>
 *   <li><strong>validateProjectTransition:</strong> Valida transiciones de estado</li>
 * </ul>
 * 
 * @see <a href="../../../../../docs/domain/project.md">Documentación completa del dominio</a>
 * @author diegoarnanz-maker
 * @version 1.0
 * @since 2025
 */
@Service
public class ProjectDomainService {
    
    // Constante para el límite máximo de proyectos por usuario
    private static final int MAX_PROJECTS_PER_USER = 5;
    
    /**
     * Valida si un usuario puede crear un nuevo proyecto
     * Solo cuenta proyectos activos en desarrollo (OPEN, IN_PROGRESS, UNDER_REVIEW)
     * @param ownerId ID del usuario propietario
     * @param currentProjectCount Número actual de proyectos activos en desarrollo del usuario
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