package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeEmailRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeAvatarRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Caso de uso para la gestión del perfil de usuario.
 */
public interface ProfileUseCase {
    
    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param username Nombre de usuario del usuario autenticado
     * @return Perfil del usuario
     */
    UserResponseDto getMyProfile(String username);

    /**
     * Actualiza el perfil de un usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos de actualización del perfil
     * @return Usuario actualizado
     */
    UserResponseDto updateProfile(Long userId, UserUpdateProfileRequestDto dto);

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos para el cambio de contraseña
     */
    void changePassword(Long userId, UserChangePasswordRequestDto dto);

    /**
     * Cambia el email de un usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos para el cambio de email
     * @return Usuario actualizado
     */
    UserResponseDto changeEmail(Long userId, UserChangeEmailRequestDto dto);

    /**
     * Cambia el avatar de un usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos para el cambio de avatar
     * @return Usuario actualizado
     */
    UserResponseDto changeAvatar(Long userId, UserChangeAvatarRequestDto dto);

    /**
     * Obtiene el perfil de un usuario.
     *
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    UserResponseDto getProfile(Long userId);
}
