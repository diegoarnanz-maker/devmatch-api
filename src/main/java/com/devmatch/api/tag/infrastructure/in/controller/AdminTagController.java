package com.devmatch.api.tag.infrastructure.in.controller;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.tag.application.port.in.AdminTagUseCase;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para operaciones administrativas de tags.
 * 
 * Este controlador expone endpoints que requieren permisos de administrador
 * para gestionar tags del sistema.
 */
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTagController {

    private final AdminTagUseCase adminTagUseCase;

    /**
     * Obtiene todos los tags disponibles en el sistema.
     *
     * @return Lista de todos los tags
     */
    @GetMapping("/admin")
    public ResponseEntity<List<AdminTagResponseDto>> getAllTags() {
        return ResponseEntity.ok(adminTagUseCase.getAllTags());
    }

    /**
     * Crea un nuevo tag en el sistema.
     *
     * @param request DTO con los datos del tag a crear
     * @return Tag creado
     */
    @PostMapping("/admin")
    public ResponseEntity<AdminTagResponseDto> createTag(@Valid @RequestBody AdminTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.createTag(request));
    }

    /**
     * Actualiza un tag existente.
     *
     * @param id ID del tag a actualizar
     * @param request DTO con los nuevos datos del tag
     * @return Tag actualizado
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<AdminTagResponseDto> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody AdminTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.updateTag(id, request));
    }

    /**
     * Elimina un tag del sistema.
     *
     * @param id ID del tag a eliminar
     * @return Respuesta vacía
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        adminTagUseCase.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    // ===== GESTIÓN DE RELACIÓN USUARIO-TAG =====

    /**
     * Obtiene los tags de un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de tags del usuario
     */
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<List<TagResponseDto>> getUserTags(@PathVariable Long userId) {
        return ResponseEntity.ok(adminTagUseCase.getUserTags(userId));
    }

    /**
     * Agrega un tag a un usuario específico.
     *
     * @param userId ID del usuario
     * @param request DTO con el ID del tag a agregar
     * @return Usuario actualizado con el nuevo tag
     */
    @PostMapping("/admin/users/{userId}")
    public ResponseEntity<UserResponseDto> addTagToUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.addTagToUser(userId, request));
    }

    /**
     * Remueve un tag de un usuario específico.
     *
     * @param userId ID del usuario
     * @param tagId ID del tag a remover
     * @return Usuario actualizado
     */
    @DeleteMapping("/admin/users/{userId}/tags/{tagId}")
    public ResponseEntity<UserResponseDto> removeTagFromUser(
            @PathVariable Long userId,
            @PathVariable Long tagId) {
        return ResponseEntity.ok(adminTagUseCase.removeTagFromUser(userId, tagId));
    }
} 