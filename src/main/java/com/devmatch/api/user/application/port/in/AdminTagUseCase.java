package com.devmatch.api.user.application.port.in;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

import java.util.List;

/**
 * Interfaz que define los casos de uso para la gestión administrativa de tags.
 * Esta interfaz forma parte de la capa de aplicación y sigue los principios de la arquitectura hexagonal.
 */
public interface AdminTagUseCase {

    // ===== GESTIÓN DE TAGS (CRUD) =====

    /**
     * Obtiene todos los tags disponibles en el sistema.
     *
     * @return Lista de todos los tags
     */
    List<AdminTagResponseDto> getAllTags();

    /**
     * Crea un nuevo tag en el sistema.
     *
     * @param request DTO con los datos del tag a crear
     * @return Tag creado
     */
    AdminTagResponseDto createTag(AdminTagRequestDto request);

    /**
     * Actualiza un tag existente.
     *
     * @param id ID del tag a actualizar
     * @param request DTO con los nuevos datos del tag
     * @return Tag actualizado
     */
    AdminTagResponseDto updateTag(Long id, AdminTagRequestDto request);

    /**
     * Elimina un tag del sistema.
     *
     * @param id ID del tag a eliminar
     */
    void deleteTag(Long id);

    // ===== GESTIÓN DE RELACIÓN USUARIO-TAG =====

    /**
     * Obtiene los tags de un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de tags del usuario
     */
    List<TagResponseDto> getUserTags(Long userId);

    /**
     * Agrega un tag a un usuario específico.
     *
     * @param userId ID del usuario
     * @param request DTO con el ID del tag a agregar
     * @return Usuario actualizado con el nuevo tag
     */
    UserResponseDto addTagToUser(Long userId, UserTagRequestDto request);

    /**
     * Remueve un tag de un usuario específico.
     *
     * @param userId ID del usuario
     * @param tagId ID del tag a remover
     * @return Usuario actualizado
     */
    UserResponseDto removeTagFromUser(Long userId, Long tagId);
} 