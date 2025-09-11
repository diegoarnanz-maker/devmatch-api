package com.devmatch.api.achievement.application.mapper;

import com.devmatch.api.achievement.domain.model.UserAchievement;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.application.dto.UserAchievementResponseDto;
import com.devmatch.api.achievement.application.dto.AchievementUnlockedResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades UserAchievement y DTOs.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public class UserAchievementMapper {
    
    /**
     * Convierte una entidad UserAchievement a UserAchievementResponseDto
     * 
     * @param userAchievement Entidad de dominio
     * @return DTO de respuesta
     */
    public static UserAchievementResponseDto toResponseDto(UserAchievement userAchievement) {
        if (userAchievement == null) {
            return null;
        }
        
        return new UserAchievementResponseDto(
            userAchievement.getId(),
            userAchievement.getUserId(),
            userAchievement.getAchievementCode().getValue(),
            null, // achievementTitle - se debe obtener del Achievement relacionado
            null, // achievementDescription - se debe obtener del Achievement relacionado
            null, // achievementPoints - se debe obtener del Achievement relacionado
            null, // achievementType - se debe obtener del Achievement relacionado
            null, // achievementIcon - se debe obtener del Achievement relacionado
            userAchievement.getAchievedAt(),
            userAchievement.isActive(),
            userAchievement.isDeleted(),
            userAchievement.getCreatedAt(),
            userAchievement.getUpdatedAt()
        );
    }
    
    /**
     * Convierte una entidad UserAchievement con su Achievement relacionado a UserAchievementResponseDto
     * 
     * @param userAchievement Entidad de dominio
     * @param achievement Achievement relacionado
     * @return DTO de respuesta completo
     */
    public static UserAchievementResponseDto toResponseDto(UserAchievement userAchievement, Achievement achievement) {
        if (userAchievement == null) {
            return null;
        }
        
        return new UserAchievementResponseDto(
            userAchievement.getId(),
            userAchievement.getUserId(),
            userAchievement.getAchievementCode().getValue(),
            achievement != null ? achievement.getTitle().getValue() : null,
            achievement != null ? achievement.getDescription().getValue() : null,
            achievement != null ? achievement.getPoints().getValue() : null,
            achievement != null ? achievement.getType().getValue() : null,
            achievement != null ? achievement.getIcon().getValue() : null,
            userAchievement.getAchievedAt(),
            userAchievement.isActive(),
            userAchievement.isDeleted(),
            userAchievement.getCreatedAt(),
            userAchievement.getUpdatedAt()
        );
    }
    
    /**
     * Convierte una lista de entidades UserAchievement a lista de UserAchievementResponseDto
     * 
     * @param userAchievements Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public static List<UserAchievementResponseDto> toResponseDtoList(List<UserAchievement> userAchievements) {
        if (userAchievements == null) {
            return null;
        }
        
        return userAchievements.stream()
            .map(UserAchievementMapper::toResponseDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte una entidad UserAchievement a AchievementUnlockedResponseDto
     * 
     * @param userAchievement Entidad de dominio
     * @param achievement Achievement relacionado
     * @return DTO de achievement desbloqueado
     */
    public static AchievementUnlockedResponseDto toUnlockedResponseDto(
            UserAchievement userAchievement, 
            Achievement achievement) {
        
        if (userAchievement == null || achievement == null) {
            return null;
        }
        
        return new AchievementUnlockedResponseDto(
            achievement.getId(),
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue(),
            achievement.getPoints().getValue(),
            achievement.getType().getValue(),
            achievement.getIcon().getValue(),
            userAchievement.getUserId(),
            userAchievement.getAchievedAt()
        );
    }
}
