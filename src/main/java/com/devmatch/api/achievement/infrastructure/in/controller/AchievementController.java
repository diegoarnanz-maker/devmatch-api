package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de achievements.
 * Proporciona endpoints para consultar el catálogo de achievements.
 */
@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
@Tag(name = "achievement-controller", description = "Endpoints para consultar el catálogo de logros disponibles")
public class AchievementController {
    
    private final AchievementManagementUseCase achievementManagementUseCase;
    
    @Operation(summary = "Obtener todos los logros activos", description = "Retorna una lista paginada de todos los logros disponibles en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<AchievementResponseDto>> getAllActiveAchievements(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=title,asc")
            Pageable pageable) {
        Page<AchievementResponseDto> achievements = achievementManagementUseCase.getAllActiveAchievementsPaginated(pageable);
        return ResponseEntity.ok(achievements);
    }
    
    @Operation(summary = "Obtener logro por ID", description = "Retorna los detalles de un logro específico usando su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logro encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    @GetMapping("/{achievementId}")
    public ResponseEntity<AchievementResponseDto> getAchievementById(
            @Parameter(description = "ID único del logro", example = "1")
            @PathVariable Long achievementId) {
        AchievementResponseDto achievement = achievementManagementUseCase.getAchievementById(achievementId);
        if (achievement != null) {
            return ResponseEntity.ok(achievement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Obtener logro por código", description = "Retorna los detalles de un logro específico usando su código único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logro encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<AchievementResponseDto> getAchievementByCode(
            @Parameter(description = "Código único del logro", example = "FIRST_PROJECT")
            @PathVariable String code) {
        AchievementResponseDto achievement = achievementManagementUseCase.getAchievementByCode(code);
        if (achievement != null) {
            return ResponseEntity.ok(achievement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Obtener logros por tipo", description = "Retorna una lista de todos los logros de un tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros del tipo especificado")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AchievementResponseDto>> getAchievementsByType(
            @Parameter(description = "Tipo de logro a filtrar", example = "PROJECT")
            @PathVariable String type) {
        List<AchievementResponseDto> achievements = achievementManagementUseCase.getAchievementsByType(type);
        return ResponseEntity.ok(achievements);
    }
}
