package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

import java.util.List;

public interface UserProfileTypeUseCase {
    
    /**
     * Obtiene los tipos de perfil del usuario autenticado.
     *
     * @param username Nombre de usuario
     * @return Lista de tipos de perfil del usuario
     */
    List<ProfileTypeResponseDto> getUserProfileTypes(String username);
    
    /**
     * Agrega un tipo de perfil al usuario autenticado.
     *
     * @param username Nombre de usuario
     * @param request DTO con el ID del tipo de perfil a agregar
     * @return Usuario actualizado
     */
    UserResponseDto addProfileType(String username, UserProfileTypeRequestDto request);
    
    /**
     * Remueve un tipo de perfil del usuario autenticado.
     *
     * @param username Nombre de usuario
     * @param profileTypeId ID del tipo de perfil a remover
     * @return Usuario actualizado
     */
    UserResponseDto removeProfileType(String username, Long profileTypeId);
} 