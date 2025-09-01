package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AdminUserAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminUserAchievementUseCase;
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
@RequestMapping("/api/admin/users/{userId}/achievements")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserAchievementController {
    
    private final AdminUserAchievementUseCase adminUserAchievementUseCase;
    
    /**
     * Obtiene todos los achievements de un usuario específico
     */
    @GetMapping
    public ResponseEntity<List<UserAchievementResponseDto>> getUserAchievements(@PathVariable Long userId) {
        List<UserAchievementResponseDto> achievements = adminUserAchievementUseCase.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Asigna un achievement a un usuario
     */
    @PostMapping
    public ResponseEntity<UserAchievementResponseDto> assignAchievement(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserAchievementRequestDto request) {
        request.setUserId(userId);
        UserAchievementResponseDto userAchievement = adminUserAchievementUseCase.assignAchievement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAchievement);
    }
    
    /**
     * Remueve un achievement de un usuario
     */
    @DeleteMapping("/{achievementCode}")
    public ResponseEntity<Void> removeAchievement(
            @PathVariable Long userId,
            @PathVariable String achievementCode) {
        adminUserAchievementUseCase.removeAchievement(userId, achievementCode);
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
    @GetMapping("/{achievementCode}/has")
    public ResponseEntity<Boolean> hasUserAchievement(
            @PathVariable Long userId,
            @PathVariable String achievementCode) {
        boolean hasAchievement = adminUserAchievementUseCase.hasUserAchievement(userId, achievementCode);
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
