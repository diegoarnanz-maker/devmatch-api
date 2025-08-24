package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
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
public class AchievementTriggerController {
    
    private final AchievementTriggerUseCase achievementTriggerUseCase;
    
    /**
     * Procesa un trigger de achievement para un usuario
     */
    @PostMapping("/process")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> processAchievementTrigger(
            @RequestBody AchievementTriggerRequestDto request) {
        List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.processAchievementTrigger(request);
        return ResponseEntity.ok(unlockedAchievements);
    }
    
    /**
     * Verifica achievements potenciales para un usuario
     */
    @GetMapping("/potential/{userId}/{achievementType}")
    public ResponseEntity<List<String>> checkPotentialAchievements(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        List<String> potentialAchievements = achievementTriggerUseCase.checkPotentialAchievements(userId, achievementType);
        return ResponseEntity.ok(potentialAchievements);
    }
    
    /**
     * Obtiene el progreso de un usuario hacia un achievement específico
     */
    @GetMapping("/progress/{userId}/{achievementType}")
    public ResponseEntity<Integer> getUserProgressTowardsAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        Integer progress = achievementTriggerUseCase.getUserProgressTowardsAchievement(userId, achievementType);
        return ResponseEntity.ok(progress);
    }
    
    /**
     * Fuerza una verificación completa de achievements para un usuario
     */
    @PostMapping("/force-check/{userId}")
    public ResponseEntity<List<AchievementUnlockedResponseDto>> forceAchievementCheck(@PathVariable Long userId) {
        List<AchievementUnlockedResponseDto> unlockedAchievements = achievementTriggerUseCase.forceAchievementCheck(userId);
        return ResponseEntity.ok(unlockedAchievements);
    }
}
