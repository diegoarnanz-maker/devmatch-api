package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementManagementUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*")
public class AchievementController {
    
    private final AchievementManagementUseCase achievementManagementUseCase;
    
    /**
     * Obtiene un achievement por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> getAchievementById(@PathVariable Long id) {
        log.info("Obteniendo achievement con ID: {}", id);
        
        try {
            AchievementResponseDto achievement = achievementManagementUseCase.getAchievementById(id);
            if (achievement != null) {
                return ResponseEntity.ok(achievement);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error obteniendo achievement con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene un achievement por su código
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<AchievementResponseDto> getAchievementByCode(@PathVariable String code) {
        log.info("Obteniendo achievement con código: {}", code);
        
        try {
            AchievementResponseDto achievement = achievementManagementUseCase.getAchievementByCode(code);
            if (achievement != null) {
                return ResponseEntity.ok(achievement);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error obteniendo achievement con código {}: {}", code, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene todos los achievements activos
     */
    @GetMapping
    public ResponseEntity<List<AchievementResponseDto>> getAllActiveAchievements() {
        log.info("Obteniendo todos los achievements activos");
        
        try {
            List<AchievementResponseDto> achievements = achievementManagementUseCase.getAllActiveAchievements();
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            log.error("Error obteniendo achievements activos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene achievements por tipo
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AchievementResponseDto>> getAchievementsByType(@PathVariable String type) {
        log.info("Obteniendo achievements del tipo: {}", type);
        
        try {
            List<AchievementResponseDto> achievements = achievementManagementUseCase.getAchievementsByType(type);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            log.error("Error obteniendo achievements del tipo {}: {}", type, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
