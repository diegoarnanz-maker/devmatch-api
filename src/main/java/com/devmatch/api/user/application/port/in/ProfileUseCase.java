package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserProfileResponseDto;
import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;

/**
 * Casos de uso relacionados con la gestión del perfil del usuario autenticado.
 *
 * Esta interfaz define las operaciones que permiten a un usuario consultar
 * su perfil, actualizar su información personal y cambiar su contraseña.
 */
public interface ProfileUseCase {

    /**
     * Obtiene el perfil del usuario autenticado a partir de su ID.
     *
     * @param userId ID del usuario.
     * @return DTO con los datos del perfil del usuario.
     */
    UserProfileResponseDto getProfile(Long userId);

    /**
     * Actualiza los datos personales del usuario autenticado.
     *
     * @param userId ID del usuario que realiza la modificación.
     * @param dto    DTO con los nuevos valores para el perfil.
     * @return DTO actualizado con los datos modificados del usuario.
     */
    UserProfileResponseDto updateProfile(Long userId, UserUpdateProfileRequestDto dto);

    /**
     * Permite al usuario cambiar su contraseña actual.
     *
     * @param userId ID del usuario que solicita el cambio.
     * @param dto    DTO con la contraseña actual y la nueva contraseña.
     */
    void changePassword(Long userId, UserChangePasswordRequestDto dto);
}
