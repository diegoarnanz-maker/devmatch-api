package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
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
     * Gestiona el rol de administrador de un usuario.
     * 
     * Casos posibles:
     * - Si el usuario no tiene rol ADMIN: se le agrega
     * - Si el usuario ya tiene rol ADMIN: no se hace ningún cambio (idempotencia)
     * - Si el usuario tiene otros roles: se mantienen y se agrega ADMIN
     *
     * @param userId ID del usuario
     * @return Usuario actualizado
     */
    @PostMapping("/admin-role/{userId}")
    public ResponseEntity<UserResponseDto> manageAdminRole(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserManagementUseCase.manageAdminRole(userId));
    }

    /**
     * Activa o desactiva un usuario.
     *
     * @param userId ID del usuario
     * @param active Estado deseado (true = activo, false = inactivo)
     * @return Usuario actualizado
     */
    @PutMapping("/status/{userId}")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody boolean active) {
        return ResponseEntity.ok(adminUserManagementUseCase.updateUserStatus(userId, active));
    }

    /**
     * Elimina un usuario (soft delete).
     *
     * @param userId ID del usuario
     * @return Usuario actualizado con estado deleted
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long userId) {
        adminUserManagementUseCase.deleteUser(userId);
        // Obtener el usuario actualizado para devolverlo
        UserResponseDto deletedUser = userQueryUseCase.findUserById(userId);
        return ResponseEntity.ok(deletedUser);
    }
} 