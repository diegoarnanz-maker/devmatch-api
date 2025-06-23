package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.dto.admin.UpdateUserRoleRequestDto;

/**
 * Casos de uso destinados a la gestión administrativa de usuarios.
 * 
 * Estos métodos permiten a un administrador consultar información sensible 
 * (incluso de usuarios desactivados o eliminados) y modificar roles de usuarios.
 */
public interface AdminUserManagementUseCase {

    /**
     * Devuelve los detalles de un usuario, incluso si está desactivado o marcado
     * como eliminado.
     * Este método está destinado exclusivamente para uso administrativo.
     * 
     * @param userId ID del usuario que se desea consultar.
     * @return Un DTO con la información del usuario.
     */
    UserResponseDto getUserDetailsForAdmin(Long userId);

    /**
     * Cambia el rol de un usuario de forma flexible.
     * 
     * @param userId ID del usuario al que cambiar el rol
     * @param request DTO con el nuevo rol a asignar
     * @return Usuario actualizado con el nuevo rol
     */
    UserResponseDto updateUserRole(Long userId, UpdateUserRoleRequestDto request);

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
}
