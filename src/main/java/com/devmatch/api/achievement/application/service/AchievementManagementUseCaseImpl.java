package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.in.AchievementManagementUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.mapper.AchievementMapper;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso para gestión de achievements.
 * Proporciona operaciones de consulta de achievements (solo lectura).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AchievementManagementUseCaseImpl implements AchievementManagementUseCase {
    
    private final AchievementRepository achievementRepository;
    
    @Override
    public AchievementResponseDto getAchievementById(Long id) {
        log.debug("Obteniendo achievement por ID: {}", id);
        
        Achievement achievement = achievementRepository.findById(id)
            .orElseThrow(() -> new AchievementNotFoundException(id));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public AchievementResponseDto getAchievementByCode(String code) {
        log.debug("Obteniendo achievement por código: {}", code);
        
        Achievement achievement = achievementRepository.findByCode(code)
            .orElseThrow(() -> new AchievementNotFoundException(code));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public List<AchievementResponseDto> getAllActiveAchievements() {
        log.debug("Obteniendo todos los achievements activos");
        
        List<Achievement> achievements = achievementRepository.findAllActive();
        return AchievementMapper.toResponseDtoList(achievements);
    }
    
    @Override
    public List<AchievementResponseDto> getAchievementsByType(String type) {
        log.debug("Obteniendo achievements por tipo: {}", type);
        
        List<Achievement> achievements = achievementRepository.findByType(type);
        return AchievementMapper.toResponseDtoList(achievements);
    }
}
