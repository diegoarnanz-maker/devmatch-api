package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.admin.UpdateUserRoleDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Casos de uso destinados a la gestión administrativa de usuarios.
 * 
 * Estos métodos permiten a un administrador consultar información sensible 
 * (incluso de usuarios desactivados o eliminados) y modificar roles de usuarios.
 */
public interface AdminUserManagementUseCase {

    /**
     * Actualiza el rol de un usuario específico, identificado por su ID.
     * 
     * @param userId ID del usuario cuyo rol se desea actualizar.
     * @param dto DTO con el nuevo rol a asignar.
     * @return Usuario actualizado.
     */
    UserResponseDto updateUserRole(Long userId, UpdateUserRoleDto dto);

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
