package com.devmatch.api.tag.infrastructure.in.controller;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.port.in.AdminTagUseCase;
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
     * Obtiene todos los tags disponibles en el sistema (incluyendo eliminados).
     *
     * @return Lista de todos los tags
     */
    @GetMapping("/admin")
    public ResponseEntity<List<AdminTagResponseDto>> getAllTags() {
        return ResponseEntity.ok(adminTagUseCase.getAllTags());
    }

    /**
     * Obtiene solo los tags activos (no eliminados).
     *
     * @return Lista de tags activos
     */
    @GetMapping("/admin/active")
    public ResponseEntity<List<AdminTagResponseDto>> getActiveTags() {
        return ResponseEntity.ok(adminTagUseCase.getActiveTags());
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
     * @param tagId   ID del tag a actualizar
     * @param request DTO con los nuevos datos del tag
     * @return Tag actualizado
     */
    @PutMapping("/admin/{tagId}")
    public ResponseEntity<AdminTagResponseDto> updateTag(
            @PathVariable("tagId") Long tagId,
            @Valid @RequestBody AdminTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.updateTag(tagId, request));
    }

    /**
     * Elimina un tag del sistema.
     *
     * @param tagId ID del tag a eliminar
     * @return Respuesta vacía
     */
    @DeleteMapping("/admin/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable("tagId") Long tagId) {
        adminTagUseCase.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reactiva un tag eliminado.
     *
     * @param tagId ID del tag a reactivar
     * @return Tag reactivado
     */
    @PutMapping("/admin/{tagId}/reactivate")
    public ResponseEntity<AdminTagResponseDto> reactivateTag(@PathVariable("tagId") Long tagId) {
        return ResponseEntity.ok(adminTagUseCase.reactivateTag(tagId));
    }

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

}