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
import com.devmatch.api.achievement.domain.exception.UserAlreadyHasAchievementException;
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
    public UserAchievementResponseDto assignAchievement(Long userId, AdminUserAchievementRequestDto request) {
        log.info("Admin asignando achievement ID '{}' al usuario: {}", 
                request.getAchievementId(), userId);
        
        // Verificar que el achievement existe
        Achievement achievement = achievementRepository.findById(request.getAchievementId())
            .orElseThrow(() -> new AchievementNotFoundException(request.getAchievementId()));
        
        // Verificar que el usuario no tenga ya este achievement
        if (userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue())) {
            throw new UserAlreadyHasAchievementException(userId, request.getAchievementId());
        }
        
        // Crear y guardar el UserAchievement
        UserAchievement userAchievement = new UserAchievement(
            userId,
            achievement.getCode()
        );
        
        UserAchievement savedUserAchievement = userAchievementRepository.save(userAchievement);
        
        return UserAchievementMapper.toResponseDto(savedUserAchievement, achievement);
    }
    
    @Override
    public void removeAchievement(Long userId, Long achievementId) {
        log.info("Admin removiendo achievement ID '{}' del usuario: {}", achievementId, userId);
        
        // Buscar el achievement para obtener su código
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        UserAchievement userAchievement = userAchievementRepository
            .findByUserIdAndAchievementCode(userId, achievement.getCode().getValue())
            .orElseThrow(() -> new UserAchievementNotFoundException(userId, achievementId));
        
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
    public boolean hasUserAchievement(Long userId, Long achievementId) {
        log.debug("Admin verificando si usuario {} tiene achievement ID '{}'", userId, achievementId);
        
        // Buscar el achievement para obtener su código
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        return userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue());
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
