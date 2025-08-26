package com.devmatch.api.achievement.infrastructure.in.controller;

import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.UserAchievementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
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
public class UserAchievementController {
    
    private final UserAchievementUseCase userAchievementUseCase;
    
    /**
     * Obtiene todos los achievements del usuario autenticado
     */
    @GetMapping("/me/achievements")
    public ResponseEntity<List<UserAchievementResponseDto>> getMyAchievements(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        List<UserAchievementResponseDto> achievements = userAchievementUseCase.getUserAchievements(currentUserId);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Obtiene un achievement específico del usuario autenticado
     */
    @GetMapping("/me/achievements/{achievementCode}")
    public ResponseEntity<UserAchievementResponseDto> getMyAchievement(
            @PathVariable String achievementCode,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        UserAchievementResponseDto achievement = userAchievementUseCase.getUserAchievement(currentUserId, achievementCode);
        return ResponseEntity.ok(achievement);
    }
    
    /**
     * Verifica si el usuario autenticado tiene un achievement específico
     */
    @GetMapping("/me/achievements/{achievementCode}/has")
    public ResponseEntity<Boolean> hasMyAchievement(
            @PathVariable String achievementCode,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        boolean hasAchievement = userAchievementUseCase.hasUserAchievement(currentUserId, achievementCode);
        return ResponseEntity.ok(hasAchievement);
    }
    
    /**
     * Obtiene el total de puntos de achievements del usuario autenticado
     */
    @GetMapping("/me/achievements/points/total")
    public ResponseEntity<Integer> getMyTotalPoints(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        Long currentUserId = userPrincipal.getUserId();
        Integer totalPoints = userAchievementUseCase.getUserTotalPoints(currentUserId);
        return ResponseEntity.ok(totalPoints);
    }
}
