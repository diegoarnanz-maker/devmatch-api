package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.dto.admin.UpdateUserRoleRequestDto;
import com.devmatch.api.user.application.dto.admin.UserSearchCriteriaDto;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador para operaciones administrativas de usuarios.
 * 
 * Este controlador expone endpoints que requieren permisos de administrador
 * para gestionar usuarios del sistema.
 */
@RestController
@RequestMapping("/api/v1/users/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final UserQueryUseCase userQueryUseCase;

    /**
     * Busca usuarios por múltiples criterios opcionales, incluyendo estado.
     * La búsqueda es parcial (contiene) y todos los parámetros son opcionales.
     *
     * @param criteria DTO con los criterios de búsqueda (email, username, firstName, lastName, status)
     * @param pageable Parámetros de paginación y ordenación
     * @return Página de usuarios que coinciden con los criterios especificados
     */
    @PostMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(@RequestBody UserSearchCriteriaDto criteria, Pageable pageable) {
        return ResponseEntity.ok(userQueryUseCase.searchUsers(criteria, pageable));
    }
    
    /**
     * Obtiene los detalles de un usuario para administradores.
     * 
     * @param userId ID del usuario
     * @return Detalles del usuario
     */
    @GetMapping("/{userId}")
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
    @PutMapping("/role/{userId}")
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
    @PutMapping("/status/{userId}")
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
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long userId) {
        adminUserManagementUseCase.deleteUser(userId);
        // Obtener el usuario actualizado para devolverlo
        UserResponseDto deletedUser = userQueryUseCase.findUserById(userId);
        return ResponseEntity.ok(deletedUser);
    }
} 