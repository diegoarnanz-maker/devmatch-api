package com.devmatch.api.tag.application.port.in;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;

import java.util.List;

/**
 * Interfaz que define los casos de uso para la gestión administrativa de tags.
 * Esta interfaz forma parte de la capa de aplicación y sigue los principios de la arquitectura hexagonal.
 */
public interface AdminTagUseCase {

    // ===== GESTIÓN DE TAGS (CRUD) =====

    /**
     * Obtiene todos los tags disponibles en el sistema (incluyendo eliminados).
     *
     * @return Lista de todos los tags
     */
    List<AdminTagResponseDto> getAllTags();

    /**
     * Obtiene solo los tags activos (no eliminados).
     *
     * @return Lista de tags activos
     */
    List<AdminTagResponseDto> getActiveTags();

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

    /**
     * Reactiva un tag eliminado.
     *
     * @param id ID del tag a reactivar
     * @return Tag reactivado
     */
    AdminTagResponseDto reactivateTag(Long id);

    // ===== GESTIÓN DE RELACIÓN USUARIO-TAG =====

    /**
     * Obtiene los tags de un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de tags del usuario
     */
    List<TagResponseDto> getUserTags(Long userId);
} 