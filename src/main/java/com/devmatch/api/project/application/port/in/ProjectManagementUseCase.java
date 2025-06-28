package com.devmatch.api.project.application.port.in;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;

import java.util.List;

/**
 * Caso de uso para la gestión de proyectos (CRUD y operaciones de negocio)
 * Permite a usuarios crear, actualizar, eliminar y gestionar sus proyectos
 */
public interface ProjectManagementUseCase {

    /**
     * Crea un nuevo proyecto
     * @param request DTO con los datos del proyecto a crear
     * @param ownerId ID del usuario propietario del proyecto
     * @return DTO con los datos del proyecto creado
     */
    ProjectResponseDto createProject(ProjectRequestDto request, Long ownerId);

    /**
     * Actualiza un proyecto existente
     * @param projectId ID del proyecto a actualizar
     * @param request DTO con los nuevos datos del proyecto
     * @param userId ID del usuario que realiza la actualización
     * @return DTO con los datos del proyecto actualizado
     */
    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request, Long userId);

    /**
     * Cambia el estado de un proyecto
     * @param projectId ID del proyecto
     * @param newStatus Nuevo estado del proyecto
     * @param userId ID del usuario que realiza el cambio
     * @return DTO con los datos del proyecto actualizado
     */
    ProjectResponseDto changeProjectStatus(Long projectId, ProjectStatus newStatus, Long userId);

    /**
     * Desactiva un proyecto (soft deactivate)
     * @param projectId ID del proyecto a desactivar
     * @param userId ID del usuario que realiza la desactivación
     */
    void deactivateProject(Long projectId, Long userId);

    /**
     * Elimina un proyecto (soft delete)
     * @param projectId ID del proyecto a eliminar
     * @param userId ID del usuario que realiza la eliminación
     */
    void deleteProject(Long projectId, Long userId);

    /**
     * Obtiene todos los proyectos de un usuario
     * @param ownerId ID del propietario de los proyectos
     * @return Lista de proyectos del usuario
     */
    List<ProjectResponseDto> getProjectsByOwner(Long ownerId);

    /**
     * Obtiene un proyecto específico por su ID
     * @param projectId ID del proyecto
     * @param userId ID del usuario que solicita el proyecto
     * @return DTO con los datos del proyecto
     */
    ProjectResponseDto getProjectById(Long projectId, Long userId);

    /**
     * Obtiene un proyecto público específico por su ID
     * Solo proyectos públicos y activos
     * @param projectId ID del proyecto
     * @return Proyecto público
     */
    ProjectResponseDto getPublicProjectById(Long projectId);

    /**
     * Obtiene los miembros de un proyecto
     * @param projectId ID del proyecto
     * @param userId ID del usuario que solicita la información
     * @return Lista de miembros del proyecto
     */
    List<ProjectResponseDto.ProjectMemberDto> getProjectMembers(Long projectId, Long userId);
}
