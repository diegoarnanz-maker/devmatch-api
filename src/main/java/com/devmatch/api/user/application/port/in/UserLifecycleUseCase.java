package com.devmatch.api.user.application.port.in;

/**
 * Define los casos de uso relacionados con el ciclo de vida de los usuarios.
 * Incluye operaciones de activación, desactivación, eliminación lógica y restauración.
 */
public interface UserLifecycleUseCase {

    /**
     * Desactiva un usuario, estableciendo su estado como inactivo (is_active = false).
     *
     * @param userId ID del usuario a desactivar.
     */
    void deactivateUser(Long userId);

    /**
     * Reactiva un usuario previamente desactivado (is_active = true).
     *
     * @param userId ID del usuario a reactivar.
     */
    void reactivateUser(Long userId);

    /**
     * Marca un usuario como eliminado lógicamente (is_deleted = true).
     *
     * @param userId ID del usuario a eliminar.
     */
    void deleteUser(Long userId);

    /**
     * Restaura un usuario que había sido eliminado lógicamente (is_deleted = false).
     *
     * @param userId ID del usuario a restaurar.
     */
    void restoreUser(Long userId);

    /**
     * Elimina un usuario si no tiene rol de administrador.
     * Utilizado para reforzar reglas de negocio en la eliminación.
     *
     * @param userId ID del usuario a eliminar.
     */
    void deleteIfNotAdmin(Long userId);

    /**
     * Alterna el estado de actividad del usuario (is_active).
     * Si está activo, lo desactiva. Si está inactivo, lo activa.
     *
     * @param userId ID del usuario cuyo estado se quiere cambiar.
     */
    void toggleUserStatus(Long userId);
}
