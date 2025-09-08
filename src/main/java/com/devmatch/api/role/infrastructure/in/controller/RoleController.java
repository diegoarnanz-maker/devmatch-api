package com.devmatch.api.role.infrastructure.in.controller;

import com.devmatch.api.role.application.dto.RoleRequestDto;
import com.devmatch.api.role.application.dto.RoleResponseDto;
import com.devmatch.api.role.application.port.in.RoleManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "role-controller", description = "Endpoints administrativos para gestión de roles del sistema")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleManagementUseCase roleManagementUseCase;

    @Operation(summary = "Obtener todos los roles", description = "Obtiene todos los roles disponibles en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder")
    })
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleManagementUseCase.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener rol por ID", description = "Obtiene los detalles de un rol específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponseDto> getRoleById(
            @Parameter(description = "ID del rol", example = "1")
            @PathVariable Long roleId) {
        RoleResponseDto role = roleManagementUseCase.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden crear roles"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un rol con ese nombre")
    })
    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(
            @Parameter(description = "Datos del nuevo rol")
            @Valid @RequestBody RoleRequestDto request) {
        RoleResponseDto createdRole = roleManagementUseCase.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @Operation(summary = "Actualizar rol", description = "Actualiza los datos de un rol existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden actualizar roles"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un rol con ese nombre")
    })
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponseDto> updateRole(
            @Parameter(description = "ID del rol a actualizar", example = "1")
            @PathVariable Long roleId,
            @Parameter(description = "Datos actualizados del rol")
            @Valid @RequestBody RoleRequestDto request) {
        RoleResponseDto updatedRole = roleManagementUseCase.updateRole(roleId, request);
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema (solo si no está siendo utilizado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden eliminar roles"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - El rol está siendo utilizado por usuarios")
    })
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID del rol a eliminar", example = "1")
            @PathVariable Long roleId) {
        roleManagementUseCase.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}