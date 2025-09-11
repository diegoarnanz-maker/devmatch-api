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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión administrativa de logros de usuarios.
 * 
 * <p>Este servicio implementa la lógica de negocio para operaciones administrativas
 * sobre logros específicos de usuarios. Solo es accesible por usuarios con rol de
 * administrador y permite gestionar logros de cualquier usuario del sistema.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Consultar logros de usuarios específicos</li>
 *   <li>Asignar logros manualmente a usuarios</li>
 *   <li>Remover logros de usuarios</li>
 *   <li>Forzar verificación de logros</li>
 *   <li>Verificar posesión de logros</li>
 *   <li>Calcular puntos totales por usuario</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Valida permisos de administrador</li>
 *   <li>Verifica existencia de logros y usuarios</li>
 *   <li>Aplica reglas de negocio (no duplicados)</li>
 *   <li>Persiste cambios en repositorios</li>
 *   <li>Retorna DTOs enriquecidos</li>
 * </ol>
 * 
 * <h3>Consideraciones de negocio:</h3>
 * <ul>
 *   <li>No duplicados: Un usuario no puede tener el mismo logro dos veces</li>
 *   <li>Validaciones: Logros y usuarios deben existir</li>
 *   <li>Auditoría: Registro de operaciones administrativas</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserAchievementUseCaseImpl implements AdminUserAchievementUseCase {
    
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    
    @Override
    public List<UserAchievementResponseDto> getUserAchievements(Long userId) {
        // Obtener logros del usuario
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
        // Validar logro y crear asignación
        Achievement achievement = achievementRepository.findById(request.getAchievementId())
            .orElseThrow(() -> new AchievementNotFoundException(request.getAchievementId()));
        
        if (userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue())) {
            throw new UserAlreadyHasAchievementException(userId, request.getAchievementId());
        }
        
        UserAchievement userAchievement = new UserAchievement(
            userId,
            achievement.getCode()
        );
        
        UserAchievement savedUserAchievement = userAchievementRepository.save(userAchievement);
        
        return UserAchievementMapper.toResponseDto(savedUserAchievement, achievement);
    }
    
    @Override
    public void removeAchievement(Long userId, Long achievementId) {
        // Buscar y eliminar logro del usuario
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        UserAchievement userAchievement = userAchievementRepository
            .findByUserIdAndAchievementCode(userId, achievement.getCode().getValue())
            .orElseThrow(() -> new UserAchievementNotFoundException(userId, achievementId));
        
        userAchievementRepository.deleteById(userAchievement.getId());
    }
    
    @Override
    public List<UserAchievementResponseDto> forceAchievementCheck(Long userId) {
        // Verificación forzada de logros (implementación futura)
        return List.of();
    }
    
    @Override
    public boolean hasUserAchievement(Long userId, Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        return userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievement.getCode().getValue());
    }
    
    @Override
    public int getUserTotalPoints(Long userId) {
        // Calcular puntos totales del usuario
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
