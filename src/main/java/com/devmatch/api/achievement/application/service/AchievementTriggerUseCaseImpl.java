package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.port.out.UserAchievementRepository;
import com.devmatch.api.achievement.application.port.out.AchievementTriggerService;
import com.devmatch.api.achievement.application.port.out.AchievementEventPublisher;
import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;
import com.devmatch.api.achievement.application.mapper.UserAchievementMapper;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.model.UserAchievement;
import com.devmatch.api.achievement.domain.model.valueobject.AchievementCode;
import com.devmatch.api.achievement.domain.event.AchievementUnlockedEvent;
import com.devmatch.api.achievement.domain.event.UserAchievementEarnedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del caso de uso para triggers automáticos de achievements.
 * Maneja el desbloqueo automático de achievements basado en acciones del usuario.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AchievementTriggerUseCaseImpl implements AchievementTriggerUseCase {
    
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementTriggerService achievementTriggerService;
    private final AchievementEventPublisher eventPublisher;
    
    @Override
    public List<AchievementUnlockedResponseDto> processAchievementTrigger(AchievementTriggerRequestDto request) {
        log.info("Procesando trigger de achievement para usuario {} con tipo {}", 
                request.getUserId(), request.getAchievementType());
        
        List<AchievementUnlockedResponseDto> unlockedAchievements = new ArrayList<>();
        
        try {
            // 1. Obtener achievements del tipo especificado
            List<Achievement> achievements = achievementRepository.findByType(request.getAchievementType());
            
            for (Achievement achievement : achievements) {
                // 2. Verificar si el usuario ya tiene este achievement
                if (userAchievementRepository.existsByUserIdAndAchievementCode(
                        request.getUserId(), achievement.getCode().getValue())) {
                    continue; // Ya lo tiene, saltar
                }
                
                // 3. Verificar si cumple los criterios para desbloquear
                if (achievementTriggerService.checkAchievementCriteria(
                        request.getUserId(), achievement.getCode().getValue())) {
                    
                    // 4. Desbloquear achievement
                    UserAchievement userAchievement = unlockAchievement(request.getUserId(), achievement);
                    
                    // 5. Publicar eventos de dominio
                    publishAchievementEvents(userAchievement, achievement);
                    
                    // 6. Agregar a la lista de respuestas
                    unlockedAchievements.add(UserAchievementMapper.toUnlockedResponseDto(userAchievement, achievement));
                    
                    log.info("Achievement '{}' desbloqueado para usuario {}", 
                            achievement.getCode().getValue(), request.getUserId());
                }
            }
            
        } catch (Exception e) {
            log.error("Error procesando trigger de achievement para usuario {}: {}", 
                    request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Error procesando achievement trigger", e);
        }
        
        log.info("Trigger procesado. {} achievements desbloqueados para usuario {}", 
                unlockedAchievements.size(), request.getUserId());
        
        return unlockedAchievements;
    }
    
    @Override
    public List<String> checkPotentialAchievements(Long userId, String achievementType) {
        log.debug("Verificando achievements potenciales para usuario {} tipo {}", userId, achievementType);
        
        List<String> potentialAchievements = new ArrayList<>();
        List<Achievement> achievements = achievementRepository.findByType(achievementType);
        
        for (Achievement achievement : achievements) {
            if (!userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue())) {
                potentialAchievements.add(achievement.getCode().getValue());
            }
        }
        
        return potentialAchievements;
    }
    
    @Override
    public int getUserProgressTowardsAchievement(Long userId, String achievementType) {
        log.debug("Obteniendo progreso del usuario {} hacia achievement tipo {}", userId, achievementType);
        
        // Implementación simple: contar cuántos achievements del tipo tiene
        List<Achievement> achievements = achievementRepository.findByType(achievementType);
        int totalAchievements = achievements.size();
        int unlockedAchievements = 0;
        
        for (Achievement achievement : achievements) {
            if (userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue())) {
                unlockedAchievements++;
            }
        }
        
        return unlockedAchievements;
    }
    
    @Override
    public List<AchievementUnlockedResponseDto> forceAchievementCheck(Long userId) {
        log.info("Forzando verificación de achievements para usuario {}", userId);
        
        List<AchievementUnlockedResponseDto> unlockedAchievements = new ArrayList<>();
        
        // Verificar todos los tipos de achievements
        List<String> achievementTypes = List.of("PROJECT_CREATION", "REVIEW_SUBMISSION", "USER_REGISTRATION");
        
        for (String achievementType : achievementTypes) {
            AchievementTriggerRequestDto request = new AchievementTriggerRequestDto(userId, achievementType);
            unlockedAchievements.addAll(processAchievementTrigger(request));
        }
        
        return unlockedAchievements;
    }
    
    /**
     * Desbloquea un achievement para un usuario
     */
    private UserAchievement unlockAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = new UserAchievement(
            userId, 
            new AchievementCode(achievement.getCode().getValue())
        );
        
        return userAchievementRepository.save(userAchievement);
    }
    
    /**
     * Publica eventos de dominio relacionados con el achievement desbloqueado
     */
    private void publishAchievementEvents(UserAchievement userAchievement, Achievement achievement) {
        // Evento de achievement desbloqueado
        AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(
            userAchievement.getUserId(),
            achievement.getId(),
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue()
        );
        
        // Evento de user achievement ganado
        UserAchievementEarnedEvent earnedEvent = new UserAchievementEarnedEvent(
            userAchievement.getUserId(),
            userAchievement.getId(),
            achievement.getId(),
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue(),
            achievement.getIcon().getValue(),
            userAchievement.getAchievedAt().toString()
        );
        
        // Por ahora, solo logueamos los eventos hasta implementar el publisher
        log.info("Evento AchievementUnlockedEvent publicado: {}", unlockedEvent);
        log.info("Evento UserAchievementEarnedEvent publicado: {}", earnedEvent);
    }
}
