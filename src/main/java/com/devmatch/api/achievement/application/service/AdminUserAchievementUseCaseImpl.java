package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.dto.AdminUserAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminUserAchievementUseCase;
import com.devmatch.api.achievement.application.port.out.UserAchievementRepository;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.mapper.UserAchievementMapper;
import com.devmatch.api.achievement.domain.model.UserAchievement;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.UserAchievementNotFoundException;
import com.devmatch.api.achievement.domain.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión administrativa de achievements de usuarios.
 * Solo accesible por administradores.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserAchievementUseCaseImpl implements AdminUserAchievementUseCase {
    
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    
    @Override
    public List<UserAchievementResponseDto> getUserAchievements(Long userId) {
        log.info("Admin obteniendo achievements del usuario: {}", userId);
        
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        
        return userAchievements.stream()
            .map(userAchievement -> {
                Achievement achievement = achievementRepository.findByCode(
                    userAchievement.getAchievementCode().getValue()).orElse(null);
                return UserAchievementMapper.toResponseDto(userAchievement, achievement);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public UserAchievementResponseDto assignAchievement(AdminUserAchievementRequestDto request) {
        log.info("Admin asignando achievement '{}' al usuario: {}", 
                request.getAchievementCode(), request.getUserId());
        
        // Verificar que el achievement existe
        Achievement achievement = achievementRepository.findByCode(request.getAchievementCode())
            .orElseThrow(() -> new AchievementNotFoundException(request.getAchievementCode()));
        
        // Verificar que el usuario no tenga ya este achievement
        if (userAchievementRepository.existsByUserIdAndAchievementCode(request.getUserId(), request.getAchievementCode())) {
            throw new RuntimeException("El usuario ya tiene este achievement");
        }
        
        // Crear y guardar el UserAchievement
        UserAchievement userAchievement = new UserAchievement(
            request.getUserId(),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(request.getAchievementCode())
        );
        
        UserAchievement savedUserAchievement = userAchievementRepository.save(userAchievement);
        
        return UserAchievementMapper.toResponseDto(savedUserAchievement, achievement);
    }
    
    @Override
    public void removeAchievement(Long userId, String achievementCode) {
        log.info("Admin removiendo achievement '{}' del usuario: {}", achievementCode, userId);
        
        UserAchievement userAchievement = userAchievementRepository
            .findByUserIdAndAchievementCode(userId, achievementCode)
            .orElseThrow(() -> new UserAchievementNotFoundException(userId, achievementCode));
        
        userAchievementRepository.deleteById(userAchievement.getId());
    }
    
    @Override
    public List<UserAchievementResponseDto> forceAchievementCheck(Long userId) {
        log.info("Admin forzando verificación de achievements para usuario: {}", userId);
        
        // Por ahora, retornamos una lista vacía
        // En el futuro, esto se conectará con el sistema de triggers
        return List.of();
    }
    
    @Override
    public boolean hasUserAchievement(Long userId, String achievementCode) {
        log.debug("Admin verificando si usuario {} tiene achievement '{}'", userId, achievementCode);
        
        return userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievementCode);
    }
    
    @Override
    public int getUserTotalPoints(Long userId) {
        log.debug("Admin calculando puntos totales del usuario: {}", userId);
        
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        
        int totalPoints = 0;
        for (UserAchievement userAchievement : userAchievements) {
            Achievement achievement = achievementRepository.findByCode(
                userAchievement.getAchievementCode().getValue()).orElse(null);
            
            if (achievement != null) {
                totalPoints += achievement.getPoints().getValue();
            }
        }
        
        return totalPoints;
    }
}
