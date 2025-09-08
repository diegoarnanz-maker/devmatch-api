package com.devmatch.api.tag.infrastructure.in.controller;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.port.in.AdminTagUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
@Tag(name = "admin-tag-controller", description = "Endpoints administrativos para gestión de tags del sistema")
@SecurityRequirement(name = "bearerAuth")
public class AdminTagController {

    private final AdminTagUseCase adminTagUseCase;

    @Operation(summary = "Obtener todos los tags (Admin)", description = "Obtiene todos los tags disponibles en el sistema (incluyendo eliminados)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder")
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<AdminTagResponseDto>> getAllTags(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=name,asc")
            Pageable pageable) {
        return ResponseEntity.ok(adminTagUseCase.getAllTags(pageable));
    }

    @Operation(summary = "Obtener tags activos (Admin)", description = "Obtiene solo los tags activos (no eliminados)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags activos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder")
    })
    @GetMapping("/admin/active")
    public ResponseEntity<Page<AdminTagResponseDto>> getActiveTags(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=name,asc")
            Pageable pageable) {
        return ResponseEntity.ok(adminTagUseCase.getActiveTags(pageable));
    }

    @Operation(summary = "Crear nuevo tag (Admin)", description = "Crea un nuevo tag en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden crear tags"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tag con ese nombre")
    })
    @PostMapping("/admin")
    public ResponseEntity<AdminTagResponseDto> createTag(
            @Parameter(description = "Datos del nuevo tag")
            @Valid @RequestBody AdminTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.createTag(request));
    }

    @Operation(summary = "Actualizar tag (Admin)", description = "Actualiza un tag existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden actualizar tags"),
        @ApiResponse(responseCode = "404", description = "Tag no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tag con ese nombre")
    })
    @PutMapping("/admin/{tagId}")
    public ResponseEntity<AdminTagResponseDto> updateTag(
            @Parameter(description = "ID del tag a actualizar", example = "1")
            @PathVariable("tagId") Long tagId,
            @Parameter(description = "Datos actualizados del tag")
            @Valid @RequestBody AdminTagRequestDto request) {
        return ResponseEntity.ok(adminTagUseCase.updateTag(tagId, request));
    }

    @Operation(summary = "Eliminar tag (Admin)", description = "Elimina un tag del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tag eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden eliminar tags"),
        @ApiResponse(responseCode = "404", description = "Tag no encontrado")
    })
    @DeleteMapping("/admin/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "ID del tag a eliminar", example = "1")
            @PathVariable("tagId") Long tagId) {
        adminTagUseCase.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivar tag (Admin)", description = "Reactiva un tag eliminado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag reactivado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden reactivar tags"),
        @ApiResponse(responseCode = "404", description = "Tag no encontrado")
    })
    @PutMapping("/admin/{tagId}/reactivate")
    public ResponseEntity<AdminTagResponseDto> reactivateTag(
            @Parameter(description = "ID del tag a reactivar", example = "1")
            @PathVariable("tagId") Long tagId) {
        return ResponseEntity.ok(adminTagUseCase.reactivateTag(tagId));
    }

    @Operation(summary = "Obtener tags de usuario (Admin)", description = "Obtiene los tags de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<List<TagResponseDto>> getUserTags(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(adminTagUseCase.getUserTags(userId));
    }

}