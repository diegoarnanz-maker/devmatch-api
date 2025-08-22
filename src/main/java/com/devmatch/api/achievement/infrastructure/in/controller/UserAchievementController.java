package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.UserAchievementUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*")
public class UserAchievementController {
    
    private final UserAchievementUseCase userAchievementUseCase;
    
    /**
     * Obtiene todos los achievements de un usuario
     */
    @GetMapping
    public ResponseEntity<List<UserAchievementResponseDto>> getUserAchievements(@PathVariable Long userId) {
        log.info("Obteniendo achievements del usuario: {}", userId);
        
        try {
            List<UserAchievementResponseDto> achievements = userAchievementUseCase.getUserAchievements(userId);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            log.error("Error obteniendo achievements del usuario {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene un achievement específico de un usuario
     */
    @GetMapping("/{achievementCode}")
    public ResponseEntity<UserAchievementResponseDto> getUserAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementCode) {
        log.info("Obteniendo achievement '{}' del usuario: {}", achievementCode, userId);
        
        try {
            UserAchievementResponseDto achievement = userAchievementUseCase.getUserAchievement(userId, achievementCode);
            if (achievement != null) {
                return ResponseEntity.ok(achievement);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error obteniendo achievement '{}' del usuario {}: {}", 
                    achievementCode, userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Verifica si un usuario tiene un achievement específico
     */
    @GetMapping("/{achievementCode}/has")
    public ResponseEntity<Boolean> hasUserAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementCode) {
        log.info("Verificando si usuario {} tiene achievement '{}'", userId, achievementCode);
        
        try {
            boolean hasAchievement = userAchievementUseCase.hasUserAchievement(userId, achievementCode);
            return ResponseEntity.ok(hasAchievement);
        } catch (Exception e) {
            log.error("Error verificando si usuario {} tiene achievement '{}': {}", 
                    userId, achievementCode, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene el total de puntos de achievements de un usuario
     */
    @GetMapping("/points/total")
    public ResponseEntity<Integer> getUserTotalPoints(@PathVariable Long userId) {
        log.info("Obteniendo total de puntos del usuario: {}", userId);
        
        try {
            Integer totalPoints = userAchievementUseCase.getUserTotalPoints(userId);
            return ResponseEntity.ok(totalPoints);
        } catch (Exception e) {
            log.error("Error obteniendo total de puntos del usuario {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
