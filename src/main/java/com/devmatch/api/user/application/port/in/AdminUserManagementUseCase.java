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
     * Actualiza el rol de un usuario específico, identificado por su ID.
     * 
     * @param userId  ID del usuario cuyo rol se desea actualizar.
     * @param newRole Nuevo rol a asignar (por ejemplo, "ADMIN", "USER", etc.).
     */
    void updateUserRole(Long userId, String newRole);

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
