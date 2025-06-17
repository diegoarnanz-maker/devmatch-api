package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserProfileResponseDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Caso de uso para la gestión del perfil de usuario.
 */
public interface ProfileUseCase {
    
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
     * Obtiene el perfil de un usuario.
     *
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    UserProfileResponseDto getProfile(Long userId);
}
