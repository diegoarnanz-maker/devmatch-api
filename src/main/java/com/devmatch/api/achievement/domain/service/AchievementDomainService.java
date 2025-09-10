package com.devmatch.api.achievement.domain.service;

import com.devmatch.api.achievement.domain.model.Achievement;

/**
 * Servicio de dominio para la lógica de negocio relacionada con logros.
 * 
 * <p>Contiene las reglas de negocio complejas que no pertenecen a una sola entidad.
 * Encapsula operaciones que requieren múltiples entidades o validaciones especiales.</p>
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Validación de desbloqueo de logros</li>
 *   <li>Categorización y análisis de logros</li>
 *   <li>Lógica de negocio compleja</li>
 * </ul>
 * 
 * @see <a href="../../../../docs/domain/achievement.md">Documentación completa del dominio</a>
 * @author DevMatch Team
 * @version 1.0
 * @since 2024
 */
public class AchievementDomainService {
    
    /**
     * Verifica si un usuario puede desbloquear un logro específico.
     * 
     * @param userId ID del usuario
     * @param achievement Logro a verificar
     * @return true si puede desbloquearlo, false en caso contrario
     */
    public boolean canUnlockAchievement(Long userId, Achievement achievement) {
        // Verificar que el logro esté activo
        if (!achievement.isActive() || achievement.isDeleted()) {
            return false;
        }
        
        // Verificar que el usuario pueda desbloquear este logro
        return achievement.canBeUnlockedBy(userId);
    }
    
    /**
     * Verifica si un logro es raro o especial.
     * 
     * @param achievement Logro a verificar
     * @return true si es raro, false en caso contrario
     */
    public boolean isRareAchievement(Achievement achievement) {
        return achievement.isRare();
    }
    
    /**
     * Verifica si un logro es común o básico.
     * 
     * @param achievement Logro a verificar
     * @return true si es común, false en caso contrario
     */
    public boolean isCommonAchievement(Achievement achievement) {
        return achievement.isCommon();
    }
    
    /**
     * Calcula el tier de puntos de un logro.
     * 
     * @param achievement Logro para calcular el tier
     * @return String representando el tier (BRONZE, SILVER, GOLD, etc.)
     */
    public String getAchievementTier(Achievement achievement) {
        return achievement.getPointsTier();
    }
    
    /**
     * Verifica si un logro es apto para principiantes.
     * 
     * @param achievement Logro a verificar
     * @return true si es apto para principiantes, false en caso contrario
     */
    public boolean isBeginnerFriendly(Achievement achievement) {
        return achievement.isBeginnerFriendly();
    }
    
    /**
     * Verifica si un logro es de nivel avanzado.
     * 
     * @param achievement Logro a verificar
     * @return true si es avanzado, false en caso contrario
     */
    public boolean isAdvancedAchievement(Achievement achievement) {
        return achievement.isAdvanced();
    }
    
    /**
     * Obtiene el nombre completo del logro con puntos.
     * 
     * @param achievement Logro para obtener el nombre completo
     * @return String con el nombre y puntos del logro
     */
    public String getFullAchievementName(Achievement achievement) {
        return achievement.getFullDisplayName();
    }
    
    /**
     * Verifica si un logro es de tipo social.
     * 
     * @param achievement Logro a verificar
     * @return true si es social, false en caso contrario
     */
    public boolean isSocialAchievement(Achievement achievement) {
        return achievement.isSocial();
    }
    
    /**
     * Verifica si un logro es de tipo técnico.
     * 
     * @param achievement Logro a verificar
     * @return true si es técnico, false en caso contrario
     */
    public boolean isTechnicalAchievement(Achievement achievement) {
        return achievement.isTechnical();
    }
    
    /**
     * Verifica si un logro está relacionado con el perfil del usuario.
     * 
     * @param achievement Logro a verificar
     * @return true si está relacionado con el perfil, false en caso contrario
     */
    public boolean isProfileRelated(Achievement achievement) {
        return achievement.isProfileRelated();
    }
    
    /**
     * Verifica si un logro está relacionado con proyectos.
     * 
     * @param achievement Logro a verificar
     * @return true si está relacionado con proyectos, false en caso contrario
     */
    public boolean isProjectRelated(Achievement achievement) {
        return achievement.isProjectRelated();
    }
    
    /**
     * Verifica si un logro está relacionado con reviews.
     * 
     * @param achievement Logro a verificar
     * @return true si está relacionado con reviews, false en caso contrario
     */
    public boolean isReviewRelated(Achievement achievement) {
        return achievement.isReviewRelated();
    }
    
    /**
     * Verifica si un logro está relacionado con liderazgo.
     * 
     * @param achievement Logro a verificar
     * @return true si está relacionado con liderazgo, false en caso contrario
     */
    public boolean isLeadershipRelated(Achievement achievement) {
        return achievement.isLeadershipRelated();
    }
    
    /**
     * Verifica si un logro está relacionado con veteranos.
     * 
     * @param achievement Logro a verificar
     * @return true si está relacionado con veteranos, false en caso contrario
     */
    public boolean isVeteranRelated(Achievement achievement) {
        return achievement.isVeteranRelated();
    }
}