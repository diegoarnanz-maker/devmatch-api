package com.devmatch.api.user.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.devmatch.api.user.application.dto.admin.UserSearchCriteriaDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Casos de uso para realizar consultas relacionadas con usuarios.
 * Incluye búsquedas por rol, username y validación de existencia.
 */
public interface UserQueryUseCase {

    /**
     * Busca un usuario por su ID, incluyendo usuarios inactivos o eliminados.
     * Solo para uso administrativo.
     *
     * @param userId ID del usuario a buscar.
     * @return Usuario correspondiente.
     */
    UserResponseDto findUserById(Long userId);

    /**
     * Busca usuarios por múltiples criterios opcionales, incluyendo estado.
     * La búsqueda es parcial (contiene) y todos los parámetros son opcionales.
     * Solo para uso administrativo.
     *
     * @param criteria DTO con los criterios de búsqueda (email, username, firstName, lastName, status)
     * @param pageable Parámetros de paginación y ordenación
     * @return Página de usuarios que coinciden con los criterios especificados
     */
    Page<UserResponseDto> searchUsers(UserSearchCriteriaDto criteria, Pageable pageable);
}
