package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementManagementUseCase;
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
public class AchievementController {
    
    private final AchievementManagementUseCase achievementManagementUseCase;
    
    /**
     * Obtiene todos los achievements activos con paginación
     * 
     * @param pageable Parámetros de paginación y ordenación
     * @return Página de achievements activos
     */
    @GetMapping
    public ResponseEntity<Page<AchievementResponseDto>> getAllActiveAchievements(Pageable pageable) {
        Page<AchievementResponseDto> achievements = achievementManagementUseCase.getAllActiveAchievementsPaginated(pageable);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Obtiene un achievement por su ID
     * 
     * @param id ID único del achievement a obtener
     * @return Achievement encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> getAchievementById(@PathVariable Long id) {
        AchievementResponseDto achievement = achievementManagementUseCase.getAchievementById(id);
        if (achievement != null) {
            return ResponseEntity.ok(achievement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene un achievement por su código
     * 
     * @param code Código único del achievement a obtener
     * @return Achievement encontrado o 404 si no existe
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<AchievementResponseDto> getAchievementByCode(@PathVariable String code) {
        AchievementResponseDto achievement = achievementManagementUseCase.getAchievementByCode(code);
        if (achievement != null) {
            return ResponseEntity.ok(achievement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene achievements por tipo
     * 
     * @param type Tipo de achievement a filtrar
     * @return Lista de achievements del tipo especificado
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AchievementResponseDto>> getAchievementsByType(@PathVariable String type) {
        List<AchievementResponseDto> achievements = achievementManagementUseCase.getAchievementsByType(type);
        return ResponseEntity.ok(achievements);
    }
}
