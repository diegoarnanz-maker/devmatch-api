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
public class AdminController {

    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final AdminProfileTypeUseCase adminProfileTypeUseCase;

    /**
     * Busca usuarios por múltiples criterios opcionales, incluyendo estado.
     * La búsqueda es parcial (contiene) y todos los parámetros son opcionales.
     *
     * @param criteria DTO con los criterios de búsqueda (email, username, firstName, lastName, status)
     * @param pageable Parámetros de paginación y ordenación
     * @return Página de usuarios que coinciden con los criterios especificados
     */
    @PostMapping("/admin/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(@RequestBody UserSearchCriteriaDto criteria, Pageable pageable) {
        return ResponseEntity.ok(userQueryUseCase.searchUsers(criteria, pageable));
    }
    
    /**
     * Obtiene los detalles de un usuario para administradores.
     * 
     * @param userId ID del usuario
     * @return Detalles del usuario
     */
    @GetMapping("/{userId}/admin")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserManagementUseCase.getUserDetailsForAdmin(userId));
    }

    /**
     * Cambia el rol de un usuario de forma flexible.
     * 
     * @param userId ID del usuario al que cambiar el rol
     * @param request DTO con el nuevo rol a asignar
     * @return Usuario actualizado con el nuevo rol
     */
    @PutMapping("/{userId}/admin/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequestDto request) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserRole(userId, request));
    }

    /**
     * Actualiza el estado de activación de un usuario.
     * 
     * @param userId ID del usuario
     * @param active true para activar, false para desactivar
     * @return Usuario actualizado
     */
    @PutMapping("/{userId}/admin/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean active) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserStatus(userId, active));
    }

    /**
     * Elimina un usuario (soft delete).
     * 
     * @param userId ID del usuario a eliminar
     * @return Usuario eliminado (con isDeleted = true e isActive = false)
     */
    @DeleteMapping("/{userId}/admin")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long userId) {
        adminUserManagementUseCase.deleteUser(userId);
        // Obtener el usuario actualizado para devolverlo
        UserResponseDto deletedUser = userQueryUseCase.findUserById(userId);
        return ResponseEntity.ok(deletedUser);
    }

    // ==============================================================================
    // ENDPOINTS PARA GESTIÓN DE TIPOS DE PERFIL
    // ==============================================================================

    /**
     * Obtiene todos los tipos de perfil disponibles en el sistema.
     *
     * @return Lista de todos los tipos de perfil
     */
    @GetMapping("/admin/profile-types")
    public ResponseEntity<List<AdminProfileTypeResponseDto>> getAllProfileTypes() {
        return ResponseEntity.ok(adminProfileTypeUseCase.getAllProfileTypes());
    }

    /**
     * Crea un nuevo tipo de perfil en el sistema.
     *
     * @param request DTO con los datos del nuevo tipo de perfil
     * @return Tipo de perfil creado
     */
    @PostMapping("/admin/profile-types")
    public ResponseEntity<AdminProfileTypeResponseDto> createProfileType(
            @Valid @RequestBody AdminProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.createProfileType(request));
    }

    /**
     * Modifica un tipo de perfil existente.
     *
     * @param id ID del tipo de perfil a modificar
     * @param request DTO con los nuevos datos
     * @return Tipo de perfil modificado
     */
    @PutMapping("/admin/profile-types/{id}")
    public ResponseEntity<AdminProfileTypeResponseDto> updateProfileType(
            @PathVariable Long id,
            @Valid @RequestBody AdminProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.updateProfileType(id, request));
    }

    /**
     * Elimina un tipo de perfil del sistema (soft delete).
     *
     * @param id ID del tipo de perfil a eliminar
     * @return Respuesta vacía con código 204
     */
    @DeleteMapping("/admin/profile-types/{id}")
    public ResponseEntity<Void> deleteProfileType(@PathVariable Long id) {
        adminProfileTypeUseCase.deleteProfileType(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los tipos de perfil de un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de tipos de perfil del usuario
     */
    @GetMapping("/admin/users/{userId}/profile-types")
    public ResponseEntity<List<ProfileTypeResponseDto>> getUserProfileTypes(@PathVariable Long userId) {
        return ResponseEntity.ok(adminProfileTypeUseCase.getUserProfileTypes(userId));
    }

    /**
     * Agrega un tipo de perfil a un usuario específico.
     *
     * @param userId ID del usuario
     * @param request DTO con el ID del tipo de perfil a agregar
     * @return Usuario actualizado
     */
    @PostMapping("/admin/users/{userId}/profile-types")
    public ResponseEntity<UserResponseDto> addProfileTypeToUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileTypeRequestDto request) {
        return ResponseEntity.ok(adminProfileTypeUseCase.addProfileTypeToUser(userId, request));
    }

    /**
     * Remueve un tipo de perfil de un usuario específico.
     *
     * @param userId ID del usuario
     * @param profileTypeId ID del tipo de perfil a remover
     * @return Usuario actualizado
     */
    @DeleteMapping("/admin/users/{userId}/profile-types/{profileTypeId}")
    public ResponseEntity<UserResponseDto> removeProfileTypeFromUser(
            @PathVariable Long userId,
            @PathVariable Long profileTypeId) {
        return ResponseEntity.ok(adminProfileTypeUseCase.removeProfileTypeFromUser(userId, profileTypeId));
    }
} 