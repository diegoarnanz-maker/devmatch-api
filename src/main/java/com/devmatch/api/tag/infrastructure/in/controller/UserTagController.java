package com.devmatch.api.tag.infrastructure.in.controller;

import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.tag.application.port.in.UserTagUseCase;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
@Tag(name = "user-tag-controller", description = "Endpoints para gestión de tags de usuario")
public class UserTagController {

    private final UserTagUseCase userTagUseCase;

    @Operation(summary = "Obtener todos los tags activos", description = "Obtiene todos los tags activos disponibles en el sistema (público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<TagResponseDto>> getAllActiveTags(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=name,asc")
            Pageable pageable) {
        return ResponseEntity.ok(userTagUseCase.getAllTags(pageable));
    }

    @Operation(summary = "Buscar tags por nombre", description = "Busca tags por nombre que contengan el texto especificado (público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/search/{name}")
    public ResponseEntity<List<TagResponseDto>> searchTags(
            @Parameter(description = "Texto a buscar en el nombre del tag", example = "java")
            @PathVariable String name) {
        return ResponseEntity.ok(userTagUseCase.searchTagsByName(name));
    }

    @Operation(summary = "Obtener tags por tipo", description = "Obtiene tags por tipo específico (público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags del tipo especificado")
    })
    @GetMapping("/by-type/{tagType}")
    public ResponseEntity<List<TagResponseDto>> getTagsByType(
            @Parameter(description = "Tipo de tag a buscar", example = "TECHNOLOGY")
            @PathVariable String tagType) {
        return ResponseEntity.ok(userTagUseCase.getTagsByType(tagType));
    }

    @Operation(summary = "Obtener mis tags de perfil", description = "Obtiene todos los tags del perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tags del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile/my-tags")
    public ResponseEntity<List<TagResponseDto>> getMyProfileTags(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.getUserTags(username));
    }

    @Operation(summary = "Agregar tag a perfil", description = "Agrega un tag al perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Tag no encontrado"),
        @ApiResponse(responseCode = "409", description = "El tag ya está en tu perfil")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/profile/add")
    public ResponseEntity<UserResponseDto> addTagToProfile(
            @Parameter(description = "Datos del tag a agregar")
            @Valid @RequestBody UserTagRequestDto request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.addTag(username, request));
    }

    @Operation(summary = "Remover tag de perfil", description = "Remueve un tag del perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag removido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Tag no encontrado en tu perfil")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/profile/remove/{tagId}")
    public ResponseEntity<UserResponseDto> removeTagFromProfile(
            @Parameter(description = "ID del tag a remover", example = "1")
            @PathVariable Long tagId,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userTagUseCase.removeTag(username, tagId));
    }

} 