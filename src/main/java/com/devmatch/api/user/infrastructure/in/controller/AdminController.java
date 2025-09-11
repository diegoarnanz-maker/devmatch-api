package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.dto.admin.UpdateUserRoleRequestDto;
import com.devmatch.api.user.application.dto.admin.UserSearchCriteriaDto;
import com.devmatch.api.user.application.dto.admin.AdminProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.admin.AdminProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import com.devmatch.api.user.application.port.in.AdminProfileTypeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para operaciones administrativas de usuarios.
 * 
 * Este controlador expone endpoints que requieren permisos de administrador
 * para gestionar usuarios del sistema.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "admin-user-controller", description = "Endpoints administrativos para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final AdminProfileTypeUseCase adminProfileTypeUseCase;

    @Operation(summary = "Buscar usuarios (Admin)", description = "Busca usuarios por múltiples criterios opcionales, incluyendo estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden buscar usuarios")
    })
    @PostMapping("/admin/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(
            @Parameter(description = "Criterios de búsqueda")
            @RequestBody UserSearchCriteriaDto criteria, 
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=username,asc")
            Pageable pageable) {
        return ResponseEntity.ok(userQueryUseCase.searchUsers(criteria, pageable));
    }
    
    @Operation(summary = "Obtener detalles de usuario (Admin)", description = "Obtiene los detalles de un usuario para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles del usuario obtenidos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/admin")
    public ResponseEntity<UserResponseDto> getUserDetails(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(adminUserManagementUseCase.getUserDetailsForAdmin(userId));
    }

    @Operation(summary = "Cambiar rol de usuario (Admin)", description = "Cambia el rol de un usuario de forma flexible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden cambiar roles"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{userId}/admin/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Datos del nuevo rol")
            @Valid @RequestBody UpdateUserRoleRequestDto request) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserRole(userId, request));
    }

    @Operation(summary = "Actualizar estado de usuario (Admin)", description = "Actualiza el estado de activación de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden cambiar estados"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{userId}/admin/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "true para activar, false para desactivar", example = "true")
            @RequestParam boolean active) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserStatus(userId, active));
    }

    @Operation(summary = "Eliminar usuario (Admin)", description = "Elimina un usuario (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden eliminar usuarios"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{userId}/admin")
    public ResponseEntity<UserResponseDto> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long userId) {
        adminUserManagementUseCase.deleteUser(userId);
        // Obtener el usuario actualizado para devolverlo
        UserResponseDto deletedUser = userQueryUseCase.findUserById(userId);
        return ResponseEntity.ok(deletedUser);
    }

    @Operation(summary = "Obtener todos los tipos de perfil (Admin)", description = "Obtiene todos los tipos de perfil disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de perfil obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder")
    })
    @GetMapping("/admin/profile-types")
    public ResponseEntity<List<AdminProfileTypeResponseDto>> getAllProfileTypes() {
        return ResponseEntity.ok(adminProfileTypeUseCase.getAllProfileTypes());
    }

    @Operation(summary = "Crear tipo de perfil (Admin)", description = "Crea un nuevo tipo de perfil")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de perfil creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden crear tipos de perfil"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tipo de perfil con ese nombre")
    })
    @PostMapping("/admin/profile-types")
    public ResponseEntity<AdminProfileTypeResponseDto> createProfileType(
            @Parameter(description = "Datos del nuevo tipo de perfil")
            @Valid @RequestBody AdminProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.createProfileType(request));
    }

    @Operation(summary = "Actualizar tipo de perfil (Admin)", description = "Actualiza un tipo de perfil existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de perfil actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden actualizar tipos de perfil"),
        @ApiResponse(responseCode = "404", description = "Tipo de perfil no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tipo de perfil con ese nombre")
    })
    @PutMapping("/admin/profile-types/{profileTypeId}")
    public ResponseEntity<AdminProfileTypeResponseDto> updateProfileType(
            @Parameter(description = "ID del tipo de perfil", example = "1")
            @PathVariable Long profileTypeId,
            @Parameter(description = "Datos actualizados del tipo de perfil")
            @Valid @RequestBody AdminProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.updateProfileType(profileTypeId, request));
    }

    @Operation(summary = "Eliminar tipo de perfil (Admin)", description = "Elimina un tipo de perfil")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de perfil eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden eliminar tipos de perfil"),
        @ApiResponse(responseCode = "404", description = "Tipo de perfil no encontrado")
    })
    @DeleteMapping("/admin/profile-types/{profileTypeId}")
    public ResponseEntity<Void> deleteProfileType(
            @Parameter(description = "ID del tipo de perfil a eliminar", example = "1")
            @PathVariable Long profileTypeId) {
        adminProfileTypeUseCase.deleteProfileType(profileTypeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener tipos de perfil de usuario (Admin)", description = "Obtiene los tipos de perfil de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipos de perfil del usuario obtenidos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/admin/profile-types")
    public ResponseEntity<List<ProfileTypeResponseDto>> getUserProfileTypes(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(adminProfileTypeUseCase.getUserProfileTypes(userId));
    }

    @Operation(summary = "Agregar tipo de perfil a usuario (Admin)", description = "Agrega un tipo de perfil a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de perfil agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden gestionar tipos de perfil"),
        @ApiResponse(responseCode = "404", description = "Usuario o tipo de perfil no encontrado"),
        @ApiResponse(responseCode = "409", description = "El tipo de perfil ya está asignado al usuario")
    })
    @PostMapping("/{userId}/admin/profile-types")
    public ResponseEntity<UserResponseDto> addProfileTypeToUser(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Datos del tipo de perfil a agregar")
            @Valid @RequestBody UserProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.addProfileTypeToUser(userId, request));
    }

} 