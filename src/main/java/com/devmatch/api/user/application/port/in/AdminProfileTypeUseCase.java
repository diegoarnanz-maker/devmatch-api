package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.admin.AdminProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.admin.AdminProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

import java.util.List;

public interface AdminProfileTypeUseCase {
    
    /**
     * Obtiene todos los tipos de perfil disponibles en el sistema.
     *
     * @return Lista de todos los tipos de perfil
     */
    List<AdminProfileTypeResponseDto> getAllProfileTypes();
    
    /**
     * Crea un nuevo tipo de perfil en el sistema.
     *
     * @param request DTO con los datos del nuevo tipo de perfil
     * @return Tipo de perfil creado
     */
    AdminProfileTypeResponseDto createProfileType(AdminProfileTypeRequestDto request);
    
    /**
     * Modifica un tipo de perfil existente.
     *
     * @param id ID del tipo de perfil a modificar
     * @param request DTO con los nuevos datos
     * @return Tipo de perfil modificado
     */
    AdminProfileTypeResponseDto updateProfileType(Long id, AdminProfileTypeRequestDto request);
    
    /**
     * Elimina un tipo de perfil del sistema (soft delete).
     *
     * @param id ID del tipo de perfil a eliminar
     */
    void deleteProfileType(Long id);
    
    /**
     * Obtiene los tipos de perfil de un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de tipos de perfil del usuario
     */
    List<ProfileTypeResponseDto> getUserProfileTypes(Long userId);
    
    /**
     * Agrega un tipo de perfil a un usuario específico.
     *
     * @param userId ID del usuario
     * @param request DTO con el ID del tipo de perfil a agregar
     * @return Usuario actualizado con el nuevo tipo de perfil
     */
    UserResponseDto addProfileTypeToUser(Long userId, UserProfileTypeRequestDto request);
    
} 