package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.in.UserAchievementUseCase;
import com.devmatch.api.achievement.application.port.out.UserAchievementRepository;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.mapper.UserAchievementMapper;
import com.devmatch.api.achievement.domain.model.UserAchievement;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.UserAchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión de user achievements.
 * Proporciona operaciones relacionadas con achievements de usuarios.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserAchievementUseCaseImpl implements UserAchievementUseCase {
    
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    
    @Override
    public List<UserAchievementResponseDto> getUserAchievements(Long userId) {
        log.debug("Obteniendo achievements del usuario: {}", userId);
        
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        
        // Enriquecer con información del achievement
        return userAchievements.stream()
            .map(userAchievement -> {
                Achievement achievement = achievementRepository.findByCode(
                    userAchievement.getAchievementCode().getValue()).orElse(null);
                return UserAchievementMapper.toResponseDto(userAchievement, achievement);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public UserAchievementResponseDto getUserAchievement(Long userId, String achievementCode) {
        log.debug("Obteniendo achievement '{}' del usuario: {}", achievementCode, userId);
        
        UserAchievement userAchievement = userAchievementRepository
            .findByUserIdAndAchievementCode(userId, achievementCode)
            .orElseThrow(() -> new UserAchievementNotFoundException(userId, achievementCode));
        
        Achievement achievement = achievementRepository.findByCode(achievementCode)
            .orElse(null);
        
        return UserAchievementMapper.toResponseDto(userAchievement, achievement);
    }
    
    @Override
    public boolean hasUserAchievement(Long userId, String achievementCode) {
        log.debug("Verificando si usuario {} tiene achievement '{}'", userId, achievementCode);
        
        return userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievementCode);
    }
    
    @Override
    public int getUserTotalPoints(Long userId) {
        log.debug("Calculando puntos totales del usuario: {}", userId);
        
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
