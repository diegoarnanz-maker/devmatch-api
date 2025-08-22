package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*")
public class AchievementTriggerController {
    
    private final AchievementTriggerUseCase achievementTriggerUseCase;
    
    /**
     * Procesa un trigger de achievement para un usuario
     */
    @PostMapping("/process")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> processAchievementTrigger(
            @RequestBody AchievementTriggerRequestDto request) {
        log.info("Procesando trigger de achievement para usuario {} con tipo {}", 
                request.getUserId(), request.getAchievementType());
        
        try {
            List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.processAchievementTrigger(request);
            return ResponseEntity.ok(unlockedAchievements);
        } catch (Exception e) {
            log.error("Error procesando trigger de achievement para usuario {}: {}", 
                    request.getUserId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Verifica achievements potenciales para un usuario
     */
    @GetMapping("/potential/{userId}/{achievementType}")
    public ResponseEntity<List<String>> checkPotentialAchievements(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        log.info("Verificando achievements potenciales para usuario {} tipo {}", userId, achievementType);
        
        try {
            List<String> potentialAchievements = achievementTriggerUseCase.checkPotentialAchievements(userId, achievementType);
            return ResponseEntity.ok(potentialAchievements);
        } catch (Exception e) {
            log.error("Error verificando achievements potenciales para usuario {}: {}", 
                    userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene el progreso de un usuario hacia un achievement específico
     */
    @GetMapping("/progress/{userId}/{achievementType}")
    public ResponseEntity<Integer> getUserProgressTowardsAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        log.info("Obteniendo progreso del usuario {} hacia achievement tipo '{}'", userId, achievementType);
        
        try {
            Integer progress = achievementTriggerUseCase.getUserProgressTowardsAchievement(userId, achievementType);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            log.error("Error obteniendo progreso del usuario {} hacia achievement tipo '{}': {}", 
                    userId, achievementType, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Fuerza una verificación completa de achievements para un usuario
     */
    @PostMapping("/force-check/{userId}")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> forceAchievementCheck(@PathVariable Long userId) {
        log.info("Forzando verificación de achievements para usuario: {}", userId);
        
        try {
            List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.forceAchievementCheck(userId);
            return ResponseEntity.ok(unlockedAchievements);
        } catch (Exception e) {
            log.error("Error forzando verificación de achievements para usuario {}: {}", 
                    userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
