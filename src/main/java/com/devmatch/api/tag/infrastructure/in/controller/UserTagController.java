package com.devmatch.api.tag.infrastructure.in.controller;

import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.tag.application.port.in.UserTagUseCase;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para operaciones de tags de usuario.
 * 
 * Este controlador expone endpoints para que los usuarios puedan:
 * - Buscar y ver tags (público)
 * - Gestionar tags en su perfil (autenticado)
 */
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class UserTagController {

    private final UserTagUseCase userTagUseCase;

    /**
     * Obtiene todos los tags activos disponibles en el sistema.
     * Endpoint público - no requiere autenticación.
     *
     * @return Lista de todos los tags activos
     */
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllActiveTags() {
        return ResponseEntity.ok(userTagUseCase.getAllTags());
    }

    /**
     * Busca tags por nombre que contengan el texto especificado.
     * Endpoint público - no requiere autenticación.
     *
     * @param name Texto a buscar en el nombre del tag
     * @return Lista de tags que contengan el texto en su nombre
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<List<TagResponseDto>> searchTags(@PathVariable String name) {
        return ResponseEntity.ok(userTagUseCase.searchTagsByName(name));
    }

    /**
     * Obtiene tags por tipo específico.
     * Endpoint público - no requiere autenticación.
     *
     * @param tagType Tipo de tag a buscar
     * @return Lista de tags del tipo especificado
     */
    @GetMapping("/by-type/{tagType}")
    public ResponseEntity<List<TagResponseDto>> getTagsByType(@PathVariable String tagType) {
        return ResponseEntity.ok(userTagUseCase.getTagsByType(tagType));
    }

    /**
     * Obtiene todos los tags del perfil del usuario autenticado.
     * Requiere autenticación.
     *
     * @param authentication Información de autenticación del usuario
     * @return Lista de tags del usuario
     */
    @GetMapping("/profile/my-tags")
    public ResponseEntity<List<TagResponseDto>> getMyProfileTags(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.getUserTags(username));
    }

    /**
     * Agrega un tag al perfil del usuario autenticado.
     * Requiere autenticación.
     *
     * @param request DTO con el ID del tag a agregar
     * @param authentication Información de autenticación del usuario
     * @return Usuario actualizado con el nuevo tag
     */
    @PostMapping("/profile/add")
    public ResponseEntity<UserResponseDto> addTagToProfile(
            @Valid @RequestBody UserTagRequestDto request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.addTag(username, request));
    }

    /**
     * Remueve un tag del perfil del usuario autenticado.
     * Requiere autenticación.
     *
     * @param tagId ID del tag a remover
     * @param authentication Información de autenticación del usuario
     * @return Usuario actualizado
     */
    @DeleteMapping("/profile/remove/{tagId}")
    public ResponseEntity<UserResponseDto> removeTagFromProfile(
            @PathVariable Long tagId,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.removeTag(username, tagId));
    }
} 