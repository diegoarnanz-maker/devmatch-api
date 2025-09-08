package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AdminAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminAchievementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión administrativa de achievements.
 * Solo accesible por usuarios con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/achievements")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "admin-achievement-controller", description = "Endpoints administrativos para gestionar el catálogo de logros")
@SecurityRequirement(name = "bearerAuth")
public class AdminAchievementController {
    
    private final AdminAchievementUseCase adminAchievementUseCase;

    @Operation(summary = "Obtener todos los logros (Admin)", description = "Retorna TODOS los logros del sistema, incluyendo inactivos y eliminados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de administrador")
    })
    @GetMapping
    public ResponseEntity<Page<AchievementResponseDto>> getAllAchievements(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=createdAt,desc")
            Pageable pageable) {
        Page<AchievementResponseDto> achievements = adminAchievementUseCase.getAllAchievementsPaginated(pageable);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Obtiene un achievement específico por ID (incluyendo inactivos y eliminados)
     */
    @GetMapping("/{achievementId}")
    public ResponseEntity<AchievementResponseDto> getAchievementById(@PathVariable Long achievementId) {
        AchievementResponseDto achievement = adminAchievementUseCase.getAchievementById(achievementId);
        return ResponseEntity.ok(achievement);
    }
    
    /**
     * Busca achievements por código (incluyendo inactivos y eliminados)
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<AchievementResponseDto> getAchievementByCode(@PathVariable String code) {
        AchievementResponseDto achievement = adminAchievementUseCase.getAchievementByCode(code);
        return ResponseEntity.ok(achievement);
    }
    
    /**
     * Obtiene achievements por tipo (incluyendo inactivos y eliminados) con paginación
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<AchievementResponseDto>> getAchievementsByType(
            @PathVariable String type, 
            Pageable pageable) {
        Page<AchievementResponseDto> achievements = adminAchievementUseCase.getAchievementsByTypePaginated(type, pageable);
        return ResponseEntity.ok(achievements);
    }
    
    @Operation(summary = "Crear nuevo logro", description = "Crea un nuevo logro en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Logro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de administrador")
    })
    @PostMapping
    public ResponseEntity<AchievementResponseDto> createAchievement(
            @Parameter(description = "Datos del nuevo logro")
            @Valid @RequestBody AdminAchievementRequestDto request) {
        AchievementResponseDto achievement = adminAchievementUseCase.createAchievement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(achievement);
    }
    
    /**
     * Actualiza un achievement existente
     */
    @PutMapping("/{achievementId}")
    public ResponseEntity<AchievementResponseDto> updateAchievement(
            @PathVariable Long achievementId,
            @Valid @RequestBody AdminAchievementRequestDto request) {
        AchievementResponseDto achievement = adminAchievementUseCase.updateAchievement(achievementId, request);
        return ResponseEntity.ok(achievement);
    }
    
    /**
     * Activa/desactiva un achievement
     */
    @PatchMapping("/{achievementId}/toggle-status")
    public ResponseEntity<AchievementResponseDto> toggleAchievementStatus(@PathVariable Long achievementId) {
        AchievementResponseDto achievement = adminAchievementUseCase.toggleAchievementStatus(achievementId);
        return ResponseEntity.ok(achievement);
    }
    
    /**
     * Elimina (soft delete) un achievement
     * Los usuarios que ya lo tienen NO lo pierden, solo no pueden obtenerlo nuevos usuarios
     */
    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long achievementId) {
        adminAchievementUseCase.deleteAchievement(achievementId);
        return ResponseEntity.noContent().build();
    }
}
