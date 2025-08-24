package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.UserAchievementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de achievements de usuarios.
 * Proporciona endpoints para consultar los achievements obtenidos por los usuarios.
 */
@RestController
@RequestMapping("/api/v1/users/{userId}/achievements")
@RequiredArgsConstructor
public class UserAchievementController {
    
    private final UserAchievementUseCase userAchievementUseCase;
    
    /**
     * Obtiene todos los achievements de un usuario
     */
    @GetMapping
    public ResponseEntity<List<UserAchievementResponseDto>> getUserAchievements(@PathVariable Long userId) {
        List<UserAchievementResponseDto> achievements = userAchievementUseCase.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Obtiene un achievement específico de un usuario
     */
    @GetMapping("/{achievementCode}")
    public ResponseEntity<UserAchievementResponseDto> getUserAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementCode) {
        UserAchievementResponseDto achievement = userAchievementUseCase.getUserAchievement(userId, achievementCode);
        if (achievement != null) {
            return ResponseEntity.ok(achievement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verifica si un usuario tiene un achievement específico
     */
    @GetMapping("/{achievementCode}/has")
    public ResponseEntity<Boolean> hasUserAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementCode) {
        boolean hasAchievement = userAchievementUseCase.hasUserAchievement(userId, achievementCode);
        return ResponseEntity.ok(hasAchievement);
    }
    
    /**
     * Obtiene el total de puntos de achievements de un usuario
     */
    @GetMapping("/points/total")
    public ResponseEntity<Integer> getUserTotalPoints(@PathVariable Long userId) {
        Integer totalPoints = userAchievementUseCase.getUserTotalPoints(userId);
        return ResponseEntity.ok(totalPoints);
    }
}
