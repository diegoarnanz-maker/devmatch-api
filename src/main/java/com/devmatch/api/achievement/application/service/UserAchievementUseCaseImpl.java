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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión de logros de usuarios.
 * 
 * <p>Este servicio actúa como orquestador entre la capa de dominio y la infraestructura,
 * implementando los casos de uso relacionados con logros de usuarios. Maneja las operaciones
 * de consulta, verificación y cálculo de puntos de logros para usuarios autenticados.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Obtener logros de un usuario específico</li>
 *   <li>Verificar si un usuario tiene un logro particular</li>
 *   <li>Calcular puntos totales acumulados por usuario</li>
 *   <li>Enriquecer datos de logros con información del catálogo</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Recibe solicitudes de la capa de infraestructura (controladores)</li>
 *   <li>Consulta repositorios de dominio para obtener datos</li>
 *   <li>Aplica mappers para convertir entidades a DTOs</li>
 *   <li>Retorna datos enriquecidos al cliente</li>
 * </ol>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAchievementUseCaseImpl implements UserAchievementUseCase {
    
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    
    @Override
    public List<UserAchievementResponseDto> getUserAchievements(Long userId) {
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
    public UserAchievementResponseDto getUserAchievement(Long userId, String achievementCode) {
        UserAchievement userAchievement = userAchievementRepository
            .findByUserIdAndAchievementCode(userId, achievementCode)
            .orElseThrow(() -> new UserAchievementNotFoundException(userId, achievementCode));
        
        Achievement achievement = achievementRepository.findByCode(achievementCode)
            .orElse(null);
        
        return UserAchievementMapper.toResponseDto(userAchievement, achievement);
    }
    
    @Override
    public boolean hasUserAchievement(Long userId, String achievementCode) {
        return userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievementCode);
    }
    
    @Override
    public int getUserTotalPoints(Long userId) {
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
