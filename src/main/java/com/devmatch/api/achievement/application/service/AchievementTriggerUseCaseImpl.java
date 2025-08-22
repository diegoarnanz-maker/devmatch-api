package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.dto.AchievementProgressDto;
import com.devmatch.api.achievement.application.port.out.AchievementTriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class AchievementTriggerUseCaseImpl implements com.devmatch.api.achievement.application.port.out.AchievementTriggerService {
    
    // Mapa de criterios por código de achievement
    private final Map<String, AchievementCriteria> achievementCriteriaMap;
    
    public AchievementTriggerUseCaseImpl() {
        this.achievementCriteriaMap = initializeAchievementCriteria();
    }
    
    @Override
    public boolean checkAchievementCriteria(Long userId, String achievementCode) {
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
    
    @Override
    public AchievementProgressDto getUserProgress(Long userId, String achievementCode) {
        log.debug("Obteniendo progreso del usuario {} hacia achievement '{}'", userId, achievementCode);
        
        AchievementCriteria criteria = achievementCriteriaMap.get(achievementCode);
        if (criteria == null) {
            log.warn("No se encontraron criterios para achievement: {}", achievementCode);
            return null;
        }
        
        // Por ahora, retornamos progreso básico
        return new AchievementProgressDto(
            achievementCode,
            "Achievement " + achievementCode, // Título temporal
            "Descripción temporal", // Descripción temporal
            0, // Progreso actual
            1, // Progreso requerido
            criteria.getType()
        );
    }
    
    @Override
    public List<String> getAchievementsForTriggerType(String triggerType) {
        log.debug("Obteniendo achievements para trigger type: {}", triggerType);
        
        List<String> achievements = new ArrayList<>();
        for (Map.Entry<String, AchievementCriteria> entry : achievementCriteriaMap.entrySet()) {
            if (triggerType.equals(entry.getValue().getType())) {
                achievements.add(entry.getKey());
            }
        }
        
        return achievements;
    }
    
    @Override
    public List<String> processTrigger(Long userId, String triggerType, Object triggerData) {
        log.debug("Procesando trigger tipo '{}' para usuario {} con datos: {}", triggerType, userId, triggerData);
        
        List<String> unlockedAchievements = new ArrayList<>();
        List<String> potentialAchievements = getAchievementsForTriggerType(triggerType);
        
        for (String achievementCode : potentialAchievements) {
            if (checkAchievementCriteria(userId, achievementCode)) {
                unlockedAchievements.add(achievementCode);
            }
        }
        
        return unlockedAchievements;
    }
    
    @Override
    public Object getUserStatsForAchievementType(Long userId, String achievementType) {
        log.debug("Obteniendo estadísticas del usuario {} para achievement type: {}", userId, achievementType);
        
        // Por ahora, retornamos un mapa básico
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("achievementType", achievementType);
        stats.put("totalAchievements", 0);
        stats.put("unlockedAchievements", 0);
        
        return stats;
    }
    
    @Override
    public boolean hasUserAchievement(Long userId, String achievementCode) {
        log.debug("Verificando si usuario {} tiene achievement '{}'", userId, achievementCode);
        
        // Esta verificación se hace en el repositorio, no aquí
        // Por ahora retornamos false
        return false;
    }
    
    /**
     * Verifica criterios simples basados en el tipo de achievement
     */
    private boolean checkSimpleCriteria(Long userId, AchievementCriteria criteria) {
        switch (criteria.getType()) {
            case "USER_REGISTRATION":
                // Siempre se cumple al registrarse
                return true;
                
            case "PROJECT_CREATION":
                // Verificar si el usuario tiene al menos 1 proyecto
                return checkUserProjectCount(userId, 1);
                
            case "REVIEW_SUBMISSION":
                // Verificar si el usuario ha enviado al menos 1 review
                return checkUserReviewCount(userId, 1);
                
            case "PROJECT_COMPLETION":
                // Verificar si el usuario ha completado al menos 1 proyecto
                return checkUserCompletedProjectCount(userId, 1);
                
            default:
                log.warn("Tipo de achievement no reconocido: {}", criteria.getType());
                return false;
        }
    }
    
    /**
     * Verifica el número de proyectos del usuario
     */
    private boolean checkUserProjectCount(Long userId, int requiredCount) {
        // TODO: Implementar consulta real a la BD
        log.debug("Verificando proyectos del usuario {} (requeridos: {})", userId, requiredCount);
        return true; // Temporalmente siempre true
    }
    
    /**
     * Verifica el número de reviews del usuario
     */
    private boolean checkUserReviewCount(Long userId, int requiredCount) {
        // TODO: Implementar consulta real a la BD
        log.debug("Verificando reviews del usuario {} (requeridos: {})", userId, requiredCount);
        return true; // Temporalmente siempre true
    }
    
    /**
     * Verifica el número de proyectos completados del usuario
     */
    private boolean checkUserCompletedProjectCount(Long userId, int requiredCount) {
        // TODO: Implementar consulta real a la BD
        log.debug("Verificando proyectos completados del usuario {} (requeridos: {})", userId, requiredCount);
        return true; // Temporalmente siempre true
    }
    
    /**
     * Inicializa el mapa de criterios de achievements
     */
    private Map<String, AchievementCriteria> initializeAchievementCriteria() {
        Map<String, AchievementCriteria> criteriaMap = new HashMap<>();
        
        // Achievements de registro de usuario
        criteriaMap.put("FIRST_LOGIN", new AchievementCriteria("USER_REGISTRATION", "Primer login del usuario"));
        criteriaMap.put("PROFILE_COMPLETE", new AchievementCriteria("USER_REGISTRATION", "Perfil completo"));
        
        // Achievements de creación de proyectos
        criteriaMap.put("FIRST_PROJECT", new AchievementCriteria("PROJECT_CREATION", "Primer proyecto creado"));
        criteriaMap.put("PROJECT_MASTER", new AchievementCriteria("PROJECT_CREATION", "Múltiples proyectos creados"));
        
        // Achievements de reviews
        criteriaMap.put("FIRST_REVIEW", new AchievementCriteria("REVIEW_SUBMISSION", "Primera review enviada"));
        criteriaMap.put("REVIEW_EXPERT", new AchievementCriteria("REVIEW_SUBMISSION", "Múltiples reviews enviadas"));
        
        // Achievements de finalización de proyectos
        criteriaMap.put("PROJECT_FINISHER", new AchievementCriteria("PROJECT_COMPLETION", "Primer proyecto completado"));
        criteriaMap.put("COMPLETION_MASTER", new AchievementCriteria("PROJECT_COMPLETION", "Múltiples proyectos completados"));
        
        return criteriaMap;
    }
    
    /**
     * Clase interna para representar criterios de achievements
     */
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
