package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para triggers automáticos de achievements.
 * Proporciona endpoints para activar y verificar el progreso de achievements.
 */
@RestController
@RequestMapping("/api/v1/achievements/triggers")
@RequiredArgsConstructor
@Tag(name = "achievement-trigger-controller", description = "Endpoints para triggers automáticos y verificación de logros")
public class AchievementTriggerController {
    
    private final AchievementTriggerUseCase achievementTriggerUseCase;
    
    @Operation(summary = "Procesar trigger de logro", description = "Procesa un trigger de logro para un usuario y retorna los logros desbloqueados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trigger procesado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/process")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> processAchievementTrigger(
            @Parameter(description = "Datos del trigger de logro")
            @RequestBody AchievementTriggerRequestDto request) {
        List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.processAchievementTrigger(request);
        return ResponseEntity.ok(unlockedAchievements);
    }
    
    @Operation(summary = "Verificar logros potenciales", description = "Verifica qué logros puede obtener un usuario para un tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros potenciales obtenida exitosamente")
    })
    @GetMapping("/potential/{userId}/{achievementType}")
    public ResponseEntity<List<String>> checkPotentialAchievements(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Tipo de logro", example = "PROJECT")
            @PathVariable String achievementType) {
        List<String> potentialAchievements = achievementTriggerUseCase.checkPotentialAchievements(userId, achievementType);
        return ResponseEntity.ok(potentialAchievements);
    }
    
    @Operation(summary = "Obtener progreso hacia logro", description = "Obtiene el progreso de un usuario hacia un logro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Progreso obtenido exitosamente")
    })
    @GetMapping("/progress/{userId}/{achievementType}")
    public ResponseEntity<Integer> getUserProgressTowardsAchievement(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Tipo de logro", example = "PROJECT")
            @PathVariable String achievementType) {
        Integer progress = achievementTriggerUseCase.getUserProgressTowardsAchievement(userId, achievementType);
        return ResponseEntity.ok(progress);
    }
    
    @Operation(summary = "Forzar verificación de logros", description = "Fuerza una verificación completa de logros para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada exitosamente")
    })
    @PostMapping("/force-check/{userId}")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> forceAchievementCheck(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.forceAchievementCheck(userId);
        return ResponseEntity.ok(unlockedAchievements);
    }
}
