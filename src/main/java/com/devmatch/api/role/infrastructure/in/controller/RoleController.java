package com.devmatch.api.role.infrastructure.in.controller;

import com.devmatch.api.role.application.dto.RoleRequestDto;
import com.devmatch.api.role.application.dto.RoleResponseDto;
import com.devmatch.api.role.application.port.in.RoleManagementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para operaciones administrativas de roles.
 * 
 * Este controlador expone endpoints que requieren permisos de administrador
 * para gestionar roles del sistema.
 */
@RestController
@RequestMapping("/api/v1/roles/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleManagementUseCase roleManagementUseCase;

    /**
     * Obtiene todos los roles disponibles en el sistema.
     * 
     * @return Lista de todos los roles con sus detalles
     */
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleManagementUseCase.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtiene los detalles de un rol específico por su ID.
     * 
     * @param id ID del rol a consultar
     * @return Detalles del rol solicitado
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id) {
        RoleResponseDto role = roleManagementUseCase.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param request DTO con los datos del rol a crear (name, description)
     * @return Rol creado con su ID asignado
     */
    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto request) {
        RoleResponseDto createdRole = roleManagementUseCase.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    /**
     * Actualiza los datos de un rol existente.
     * 
     * @param id      ID del rol a actualizar
     * @param request DTO con los nuevos datos del rol (name, description)
     * @return Rol actualizado con los nuevos datos
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDto request) {
        RoleResponseDto updatedRole = roleManagementUseCase.updateRole(id, request);
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Elimina un rol del sistema.
     * Solo se puede eliminar si no está siendo utilizado por ningún usuario.
     * 
     * @param id ID del rol a eliminar
     * @return Respuesta vacía indicando que la operación fue exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleManagementUseCase.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}