package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.dto.AchievementTriggerRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;
import com.devmatch.api.achievement.application.port.in.AchievementTriggerUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.port.out.UserAchievementRepository;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.model.UserAchievement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del caso de uso de triggers para achievements.
 * Contiene la lógica de negocio para determinar cuándo un usuario puede desbloquear un achievement.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementTriggerUseCaseImpl implements AchievementTriggerUseCase {
    
    // Repositorios para persistencia
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    
    // Mapa de criterios por código de achievement
    private Map<String, AchievementCriteria> achievementCriteriaMap;
    

    
    @Override
    public List<AchievementUnlockedResponseDto> processAchievementTrigger(AchievementTriggerRequestDto request) {
        log.debug("Procesando trigger de achievement para usuario {} con tipo {}", 
                request.getUserId(), request.getAchievementType());
        
        // Inicializar criterios si es necesario
        if (achievementCriteriaMap == null) {
            achievementCriteriaMap = initializeAchievementCriteria();
        }
        
        List<AchievementUnlockedResponseDto> unlockedAchievements = new ArrayList<>();
        List<String> potentialAchievements = getAchievementsForTriggerType(request.getAchievementType());
        
        for (String achievementCode : potentialAchievements) {
            if (checkAchievementCriteria(request.getUserId(), achievementCode)) {
                // Verificar si el usuario ya tiene este achievement
                if (!userAchievementRepository.existsByUserIdAndAchievementCode(request.getUserId(), achievementCode)) {
                    // Obtener el achievement del catálogo
                    Achievement achievement = achievementRepository.findByCode(achievementCode)
                        .orElse(null);
                    
                    if (achievement != null) {
                        // Crear y guardar el UserAchievement
                        UserAchievement userAchievement = new UserAchievement(
                            request.getUserId(),
                            new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(achievementCode)
                        );
                        
                        UserAchievement savedUserAchievement = userAchievementRepository.save(userAchievement);
                        
                        // Crear el DTO de respuesta
                        unlockedAchievements.add(new AchievementUnlockedResponseDto(
                            savedUserAchievement.getId(),
                            achievementCode,
                            achievement.getTitle().getValue(),
                            achievement.getDescription().getValue(),
                            achievement.getPoints().getValue(),
                            achievement.getType().getValue(),
                            achievement.getIcon().getValue(),
                            request.getUserId(),
                            LocalDateTime.now()
                        ));
                        
                        log.info("Achievement {} desbloqueado para usuario {}", achievementCode, request.getUserId());
                    }
                }
            }
        }
        
        return unlockedAchievements;
    }
    
    @Override
    public List<String> checkPotentialAchievements(Long userId, String triggerType) {
        log.debug("Verificando achievements potenciales para usuario {} tipo {}", userId, triggerType);
        
        return getAchievementsForTriggerType(triggerType);
    }
    
    @Override
    public int getUserProgressTowardsAchievement(Long userId, String achievementType) {
        log.debug("Obteniendo progreso del usuario {} hacia achievement tipo '{}'", userId, achievementType);
        
        // Por ahora, implementación simple
        return 50; // 50% de progreso por defecto
    }
    
    @Override
    public List<AchievementUnlockedResponseDto> forceAchievementCheck(Long userId) {
        log.debug("Forzando verificación de achievements para usuario: {}", userId);
        
        // Inicializar criterios si es necesario
        if (achievementCriteriaMap == null) {
            achievementCriteriaMap = initializeAchievementCriteria();
        }
        
        List<AchievementUnlockedResponseDto> unlockedAchievements = new ArrayList<>();
        
        // Verificar todos los tipos de achievements disponibles
        for (String achievementCode : achievementCriteriaMap.keySet()) {
            if (checkAchievementCriteria(userId, achievementCode)) {
                unlockedAchievements.add(new AchievementUnlockedResponseDto(
                    null, // achievementId - se asignará desde la BD
                    achievementCode,
                    "Achievement " + achievementCode + " desbloqueado",
                    "Descripción del achievement " + achievementCode,
                    10, // achievementPoints - valor por defecto
                    "ACHIEVEMENT", // achievementType
                    "🏆", // achievementIcon - emoji por defecto
                    userId,
                    LocalDateTime.now()
                ));
            }
        }
        
        return unlockedAchievements;
    }
    
    // Métodos auxiliares privados
    private boolean checkAchievementCriteria(Long userId, String achievementCode) {
        log.debug("Verificando criterios para achievement '{}' del usuario {}", achievementCode, userId);
        
        AchievementCriteria criteria = achievementCriteriaMap.get(achievementCode);
        if (criteria == null) {
            log.warn("No se encontraron criterios para achievement: {}", achievementCode);
            return false;
        }
        
        try {
            // Por ahora, implementación simple basada en el tipo de achievement
            return checkSimpleCriteria(userId, criteria);
        } catch (Exception e) {
            log.error("Error verificando criterios para achievement {} del usuario {}: {}", 
                    achievementCode, userId, e.getMessage(), e);
            return false;
        }
    }
    
    private List<String> getAchievementsForTriggerType(String triggerType) {
        log.debug("Obteniendo achievements para trigger type: {}", triggerType);
        
        List<String> achievements = new ArrayList<>();
        for (Map.Entry<String, AchievementCriteria> entry : achievementCriteriaMap.entrySet()) {
            if (triggerType.equals(entry.getValue().getType())) {
                achievements.add(entry.getKey());
            }
        }
        
        return achievements;
    }
    
    private boolean checkSimpleCriteria(Long userId, AchievementCriteria criteria) {
        // Implementación simple por ahora
        // En el futuro, esto se conectará con servicios reales
        return true; // Por defecto, siempre retorna true para testing
    }
    
    private Map<String, AchievementCriteria> initializeAchievementCriteria() {
        Map<String, AchievementCriteria> criteriaMap = new HashMap<>();
        
        // Agregar criterios de ejemplo
        criteriaMap.put("FIRST_LOGIN", new AchievementCriteria("USER_REGISTRATION", "Primer inicio de sesión"));
        criteriaMap.put("FIRST_PROJECT", new AchievementCriteria("PROJECT_CREATION", "Crear primer proyecto"));
        criteriaMap.put("FIRST_REVIEW", new AchievementCriteria("REVIEW_SUBMISSION", "Enviar primera review"));
        criteriaMap.put("PROFILE_COMPLETE", new AchievementCriteria("PROFILE_UPDATE", "Completar perfil"));
        
        return criteriaMap;
    }
    
    // Clase interna para criterios de achievement
    private static class AchievementCriteria {
        private final String type;
        private final String description;
        
        public AchievementCriteria(String type, String description) {
            this.type = type;
            this.description = description;
        }
        
        public String getType() {
            return type;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
