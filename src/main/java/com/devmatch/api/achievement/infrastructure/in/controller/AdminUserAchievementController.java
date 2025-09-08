package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AdminUserAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminUserAchievementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión administrativa de achievements de usuarios.
 * Solo accesible por usuarios con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/users/{userId}/achievements")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "admin-user-achievement-controller", description = "Endpoints administrativos para gestionar logros de usuarios específicos")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserAchievementController {
    
    private final AdminUserAchievementUseCase adminUserAchievementUseCase;
    
    @Operation(summary = "Obtener logros de usuario", description = "Retorna todos los logros de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<UserAchievementResponseDto>> getUserAchievements(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        List<UserAchievementResponseDto> achievements = adminUserAchievementUseCase.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }
    
    @Operation(summary = "Asignar logro a usuario", description = "Asigna un logro específico a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Logro asignado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario o logro no encontrado")
    })
    @PostMapping
    public ResponseEntity<UserAchievementResponseDto> assignAchievement(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Datos del logro a asignar")
            @Valid @RequestBody AdminUserAchievementRequestDto request) {
        UserAchievementResponseDto userAchievement = adminUserAchievementUseCase.assignAchievement(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAchievement);
    }
    
    @Operation(summary = "Remover logro de usuario", description = "Remueve un logro específico de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Logro removido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario o logro no encontrado")
    })
    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> removeAchievement(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del logro a remover", example = "5")
            @PathVariable Long achievementId) {
        adminUserAchievementUseCase.removeAchievement(userId, achievementId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Fuerza la verificación de achievements para un usuario
     */
    @PostMapping("/force-check")
    public ResponseEntity<List<UserAchievementResponseDto>> forceAchievementCheck(@PathVariable Long userId) {
        List<UserAchievementResponseDto> unlockedAchievements = adminUserAchievementUseCase.forceAchievementCheck(userId);
        return ResponseEntity.ok(unlockedAchievements);
    }
    
    /**
     * Verifica si un usuario tiene un achievement específico
     */
    @GetMapping("/{achievementId}/has")
    public ResponseEntity<Boolean> hasUserAchievement(
            @PathVariable Long userId,
            @PathVariable Long achievementId) {
        boolean hasAchievement = adminUserAchievementUseCase.hasUserAchievement(userId, achievementId);
        return ResponseEntity.ok(hasAchievement);
    }
    
    /**
     * Obtiene el total de puntos de achievements de un usuario
     */
    @GetMapping("/points/total")
    public ResponseEntity<Integer> getUserTotalPoints(@PathVariable Long userId) {
        Integer totalPoints = adminUserAchievementUseCase.getUserTotalPoints(userId);
        return ResponseEntity.ok(totalPoints);
    }
}
