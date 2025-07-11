package com.devmatch.api.project.application.port.in;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.application.dto.ProjectPublicSearchRequestDto;
import com.devmatch.api.project.application.dto.ProjectTagsRequestDto;
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
     * Cambia la visibilidad pública de un proyecto
     * @param projectId ID del proyecto
     * @param isPublic Nueva visibilidad pública del proyecto
     * @param userId ID del usuario que realiza el cambio
     * @return DTO con los datos del proyecto actualizado
     */
    ProjectResponseDto changeProjectVisibility(Long projectId, boolean isPublic, Long userId);

    /**
     * Desactiva un proyecto (soft deactivate)
     * @param projectId ID del proyecto a desactivar
     * @param userId ID del usuario que realiza la desactivación
     * @return DTO con los datos del proyecto desactivado
     */
    ProjectResponseDto deactivateProject(Long projectId, Long userId);

    /**
     * Elimina un proyecto (soft delete)
     * @param projectId ID del proyecto a eliminar
     * @param userId ID del usuario que realiza la eliminación
     * @return DTO con los datos del proyecto eliminado
     */
    ProjectResponseDto deleteProject(Long projectId, Long userId);

    /**
     * Restaura un proyecto (desactivado o eliminado)
     * @param projectId ID del proyecto a restaurar
     * @param userId ID del usuario que realiza la restauración
     * @return DTO con los datos del proyecto restaurado
     */
    ProjectResponseDto restoreProject(Long projectId, Long userId);

    /**
     * Obtiene todos los proyectos de un usuario
     * @param ownerId ID del propietario de los proyectos
     * @return Lista de proyectos del usuario
     */
    List<ProjectResponseDto> getProjectsByOwner(Long ownerId);

    /**
     * Obtiene proyectos de un usuario específico con filtros y lógica de seguridad
     * Solo devuelve proyectos públicos o propios del usuario autenticado
     * @param ownerId ID del propietario de los proyectos
     * @param authenticatedUserId ID del usuario autenticado que solicita los proyectos
     * @param filter DTO con los criterios de búsqueda y filtrado (opcional)
     * @return Lista de proyectos que el usuario autenticado puede ver
     */
    List<ProjectResponseDto> getProjectsByOwnerWithSecurity(Long ownerId, Long authenticatedUserId, ProjectPublicSearchRequestDto filter);

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
     * Obtiene todos los proyectos públicos
     * Solo proyectos públicos y activos
     * @return Lista de todos los proyectos públicos
     */
    List<ProjectResponseDto> getAllPublicProjects();

    /**
     * Busca y filtra proyectos públicos con criterios múltiples
     * @param filter DTO con los criterios de búsqueda y filtrado
     * @return Lista de proyectos públicos que coinciden con los filtros
     */
    List<ProjectResponseDto> searchPublicProjects(ProjectPublicSearchRequestDto filter);

    /**
     * Agrega tags a un proyecto
     * @param projectId ID del proyecto
     * @param request DTO con los tags a agregar
     * @param userId ID del usuario que realiza la operación
     * @return DTO del proyecto actualizado
     */
    ProjectResponseDto addTagsToProject(Long projectId, ProjectTagsRequestDto request, Long userId);

    /**
     * Remueve un tag específico de un proyecto
     * @param projectId ID del proyecto
     * @param tagName Nombre del tag a remover
     * @param userId ID del usuario que realiza la operación
     * @return DTO del proyecto actualizado
     */
    ProjectResponseDto removeTagFromProject(Long projectId, String tagName, Long userId);

    /**
     * Obtiene los miembros de un proyecto
     * @param projectId ID del proyecto
     * @param userId ID del usuario que solicita la información
     * @return Lista de miembros del proyecto
     */
    List<ProjectResponseDto.ProjectMemberDto> getProjectMembers(Long projectId, Long userId);

    /**
     * Remueve un miembro del proyecto
     * @param projectId ID del proyecto
     * @param memberUserId ID del usuario miembro a remover
     * @param ownerUserId ID del propietario del proyecto que realiza la operación
     */
    void removeProjectMember(Long projectId, Long memberUserId, Long ownerUserId);

    /**
     * Cambia el rol de un miembro del proyecto
     * @param projectId ID del proyecto
     * @param memberUserId ID del usuario miembro
     * @param newRole Nuevo rol del miembro
     * @param ownerUserId ID del propietario del proyecto que realiza la operación
     * @return DTO con los datos del miembro actualizado
     */
    ProjectResponseDto.ProjectMemberDto changeMemberRole(Long projectId, Long memberUserId, String newRole, Long ownerUserId);
}
