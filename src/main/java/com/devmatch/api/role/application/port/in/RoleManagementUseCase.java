package com.devmatch.api.role.application.port.in;

import com.devmatch.api.role.application.dto.RoleRequestDto;
import com.devmatch.api.role.application.dto.RoleResponseDto;

import java.util.List;

/**
 * Caso de uso para la gestión de roles (CRUD)
 * Permite a administradores crear, consultar, actualizar y eliminar roles
 */
public interface RoleManagementUseCase {
    
    /**
     * Crea un nuevo rol
     * @param request DTO con los datos del rol a crear
     * @return DTO con los datos del rol creado
     */
    RoleResponseDto createRole(RoleRequestDto request);
    
    /**
     * Obtiene todos los roles disponibles
     * @return Lista de DTOs con todos los roles
     */
    List<RoleResponseDto> getAllRoles();
    
    /**
     * Obtiene un rol específico por su ID
     * @param id ID del rol a consultar
     * @return DTO con los datos del rol
     */
    RoleResponseDto getRoleById(Long id);
    
    /**
     * Actualiza un rol existente
     * @param id ID del rol a actualizar
     * @param request DTO con los nuevos datos del rol
     * @return DTO con los datos del rol actualizado
     */
    RoleResponseDto updateRole(Long id, RoleRequestDto request);
    
    /**
     * Elimina un rol que no esté en uso
     * @param id ID del rol a eliminar
     */
    void deleteRole(Long id);
} 