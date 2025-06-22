package com.devmatch.api.user.application.port.in;

/**
 * Caso de uso para la gestión del ciclo de vida de usuarios.
 * Define las operaciones relacionadas con la activación, desactivación y eliminación de usuarios.
 */
public interface UserLifecycleUseCase {
    
    /**
     * Desactiva un usuario por su ID.
     * @param userId ID del usuario a desactivar
     */
    void deactivateUser(Long userId);

    /**
     * Reactiva un usuario por su ID.
     * @param userId ID del usuario a reactivar
     */
    void reactivateUser(Long userId);

    /**
     * Elimina un usuario por su ID (soft delete).
     * @param userId ID del usuario a eliminar
     */
    void deleteUser(Long userId);

    /**
     * Restaura un usuario eliminado por su ID.
     * @param userId ID del usuario a restaurar
     */
    void restoreUser(Long userId);

    /**
     * Elimina un usuario si no es administrador.
     * @param userId ID del usuario a eliminar
     * @throws UserOperationNotAllowedException si el usuario es administrador
     */
    void deleteIfNotAdmin(Long userId);

    /**
     * Alterna el estado activo/inactivo de un usuario.
     * @param userId ID del usuario
     */
    void toggleUserStatus(Long userId);
} 