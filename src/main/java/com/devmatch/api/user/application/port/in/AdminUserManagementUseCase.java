package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Casos de uso destinados a la gestión administrativa de usuarios.
 * 
 * Estos métodos permiten a un administrador consultar información sensible 
 * (incluso de usuarios desactivados o eliminados) y modificar roles de usuarios.
 */
public interface AdminUserManagementUseCase {

    /**
     * Gestiona el rol de administrador de un usuario.
     * 
     * Casos posibles:
     * - Si el usuario no tiene rol ADMIN: se le agrega
     * - Si el usuario ya tiene rol ADMIN: no se hace ningún cambio
     * - Si el usuario tiene otros roles: se mantienen y se agrega ADMIN
     * 
     * @param userId ID del usuario al que gestionar el rol admin
     * @return Usuario actualizado con sus roles
     */
    UserResponseDto manageAdminRole(Long userId);

    /**
     * Actualiza el estado de activación de un usuario.
     * 
     * @param userId ID del usuario.
     * @param active true para activar, false para desactivar.
     * @return Usuario actualizado.
     */
    UserResponseDto updateUserStatus(Long userId, boolean active);

    /**
     * Elimina un usuario (soft delete).
     * 
     * @param userId ID del usuario a eliminar.
     */
    void deleteUser(Long userId);

    /**
     * Devuelve los detalles de un usuario, incluso si está desactivado o marcado
     * como eliminado.
     * Este método está destinado exclusivamente para uso administrativo.
     * 
     * @param userId ID del usuario que se desea consultar.
     * @return Un DTO con la información del usuario.
     */
    UserResponseDto getUserDetailsForAdmin(Long userId);
}
