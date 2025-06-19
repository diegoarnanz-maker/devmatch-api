package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.admin.UpdateUserRoleDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión administrativa de usuarios.
 * Solo accesible por usuarios con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/users/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final UserQueryUseCase userQueryUseCase;

    /**
     * Obtiene todos los usuarios del sistema con filtro opcional por estado.
     *
     * @param status Estado de los usuarios a filtrar (active, inactive, deleted). Si es null, devuelve todos.
     * @return Lista de usuarios filtrados
     */
    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(userQueryUseCase.findUsersByStatus(status));
    }

    /**
     * Obtiene un usuario específico por ID.
     *
     * @param userId ID del usuario
     * @return Usuario encontrado
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userQueryUseCase.findUserById(userId));
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos para actualizar el rol
     * @return Usuario actualizado
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleDto dto) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserRole(userId, dto));
    }

    /**
     * Activa o desactiva un usuario.
     *
     * @param userId ID del usuario
     * @param active Estado deseado (true = activo, false = inactivo)
     * @return Usuario actualizado
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean active) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserStatus(userId, active));
    }

    /**
     * Elimina un usuario (soft delete).
     *
     * @param userId ID del usuario
     * @return Respuesta vacía con código 204
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminUserManagementUseCase.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
} 