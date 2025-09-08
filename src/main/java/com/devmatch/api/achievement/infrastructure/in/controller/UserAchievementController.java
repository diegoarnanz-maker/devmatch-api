package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.UserAchievementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de achievements de usuarios.
 * Proporciona endpoints para consultar los achievements del usuario autenticado.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "user-achievement-controller", description = "Endpoints para gestionar los logros del usuario autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UserAchievementController {
    
    private final UserAchievementUseCase userAchievementUseCase;
    
    @Operation(summary = "Obtener mis logros", description = "Retorna todos los logros obtenidos por el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de logros del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/me/achievements")
    public ResponseEntity<List<UserAchievementResponseDto>> getMyAchievements(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        List<UserAchievementResponseDto> achievements = userAchievementUseCase.getUserAchievements(currentUserId);
        return ResponseEntity.ok(achievements);
    }
    
    @Operation(summary = "Obtener un logro específico", description = "Retorna los detalles de un logro específico obtenido por el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logro del usuario obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/me/achievements/{achievementCode}")
    public ResponseEntity<UserAchievementResponseDto> getMyAchievement(
            @Parameter(description = "Código del logro a consultar", example = "FIRST_PROJECT")
            @PathVariable String achievementCode,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        UserAchievementResponseDto achievement = userAchievementUseCase.getUserAchievement(currentUserId, achievementCode);
        return ResponseEntity.ok(achievement);
    }
    
    @Operation(summary = "Verificar si tengo un logro", description = "Verifica si el usuario autenticado ha obtenido un logro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/me/achievements/{achievementCode}/has")
    public ResponseEntity<Boolean> hasMyAchievement(
            @Parameter(description = "Código del logro a verificar", example = "FIRST_PROJECT")
            @PathVariable String achievementCode,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        boolean hasAchievement = userAchievementUseCase.hasUserAchievement(currentUserId, achievementCode);
        return ResponseEntity.ok(hasAchievement);
    }
    
    @Operation(summary = "Obtener total de puntos", description = "Retorna el total de puntos acumulados por el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total de puntos obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/me/achievements/points/total")
    public ResponseEntity<Integer> getMyTotalPoints(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        Integer totalPoints = userAchievementUseCase.getUserTotalPoints(currentUserId);
        return ResponseEntity.ok(totalPoints);
    }
}
