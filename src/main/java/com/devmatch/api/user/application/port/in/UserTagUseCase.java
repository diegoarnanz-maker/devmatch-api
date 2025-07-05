package com.devmatch.api.user.application.port.in;

import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

import java.util.List;

/**
 * Interfaz que define los casos de uso para la gestión de tags de usuario.
 * Esta interfaz forma parte de la capa de aplicación y sigue los principios de la arquitectura hexagonal.
 */
public interface UserTagUseCase {

    /**
     * Obtiene todos los tags disponibles en el sistema.
     * Este método puede ser usado por usuarios no autenticados para ver el catálogo de tags.
     *
     * @return Lista de todos los tags disponibles
     */
    List<TagResponseDto> getAllTags();

    /**
     * Obtiene todos los tags asignados al usuario autenticado.
     *
     * @param username Nombre de usuario del usuario autenticado
     * @return Lista de tags asignados al usuario
     */
    List<TagResponseDto> getUserTags(String username);

    /**
     * Agrega un tag al usuario autenticado.
     *
     * @param username Nombre de usuario del usuario autenticado
     * @param request DTO con el ID del tag a agregar
     * @return Usuario actualizado con el nuevo tag
     */
    UserResponseDto addTag(String username, UserTagRequestDto request);

    /**
     * Remueve un tag del usuario autenticado.
     *
     * @param username Nombre de usuario del usuario autenticado
     * @param tagId ID del tag a remover
     * @return Usuario actualizado
     */
    UserResponseDto removeTag(String username, Long tagId);
} 