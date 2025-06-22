package com.devmatch.api.user.application.port.in;

import java.util.List;
import java.util.Optional;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Casos de uso para realizar consultas relacionadas con usuarios.
 * Incluye búsquedas por rol, username y validación de existencia.
 */
public interface UserQueryUseCase {

    /**
     * Obtiene todos los usuarios que están activos y no han sido eliminados.
     *
     * @return Lista de usuarios visibles para operaciones estándar.
     */
    List<UserResponseDto> getAllActiveUsers();

    /**
     * Obtiene todos los usuarios del sistema (incluyendo inactivos y eliminados).
     * Solo para uso administrativo.
     *
     * @return Lista de todos los usuarios.
     */
    List<UserResponseDto> findAllUsers();

    /**
     * Busca un usuario por su username, solo si está activo y no eliminado.
     *
     * @param username Nombre de usuario a buscar.
     * @return Usuario correspondiente, si existe y está activo.
     */
    Optional<UserResponseDto> findActiveUserByUsername(String username);

    /**
     * Busca un usuario por su ID, incluyendo usuarios inactivos o eliminados.
     * Solo para uso administrativo.
     *
     * @param userId ID del usuario a buscar.
     * @return Usuario correspondiente.
     */
    UserResponseDto findUserById(Long userId);

    /**
     * Devuelve todos los usuarios activos y no eliminados con un rol específico.
     *
     * @param role Rol a filtrar.
     * @return Lista de usuarios filtrados por rol.
     */
    List<UserResponseDto> findActiveUsersByRole(String role);

    /**
     * Verifica si existe algún usuario con el username especificado,
     * independientemente de su estado.
     *
     * @param username Nombre de usuario.
     * @return true si existe, false en caso contrario.
     */
    boolean checkUsernameExists(String username);

    /**
     * Verifica si existe algún usuario con el email especificado,
     * independientemente de su estado.
     *
     * @param email Dirección de correo electrónico.
     * @return true si existe, false en caso contrario.
     */
    boolean checkEmailExists(String email);

    /**
     * Obtiene usuarios filtrados por estado.
     * Solo para uso administrativo.
     *
     * @param status Estado de los usuarios a filtrar (active, inactive, deleted). Si es null, devuelve todos.
     * @return Lista de usuarios filtrados.
     */
    List<UserResponseDto> findUsersByStatus(String status);
}
