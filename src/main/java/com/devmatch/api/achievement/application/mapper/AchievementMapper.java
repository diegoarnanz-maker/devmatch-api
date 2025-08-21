package com.devmatch.api.achievement.application.mapper;

import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades Achievement y DTOs.
 * Proporciona métodos para mapear en ambas direcciones.
 */
public class AchievementMapper {
    
    /**
     * Convierte una entidad Achievement a AchievementResponseDto
     * 
     * @param achievement Entidad de dominio
     * @return DTO de respuesta
     */
    public static AchievementResponseDto toResponseDto(Achievement achievement) {
        if (achievement == null) {
            return null;
        }
        
        return new AchievementResponseDto(
            achievement.getId(),
            achievement.getCode().getValue(),
            achievement.getTitle().getValue(),
            achievement.getDescription().getValue(),
            achievement.getPoints().getValue(),
            achievement.getType().getValue(),
            achievement.getIcon().getValue(),
            achievement.isActive(),
            achievement.isDeleted(),
            achievement.getCreatedAt(),
            achievement.getUpdatedAt()
        );
    }
    
    /**
     * Convierte una lista de entidades Achievement a lista de AchievementResponseDto
     * 
     * @param achievements Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public static List<AchievementResponseDto> toResponseDtoList(List<Achievement> achievements) {
        if (achievements == null) {
            return null;
        }
        
        return achievements.stream()
            .map(AchievementMapper::toResponseDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte un AchievementResponseDto a entidad Achievement
     * Nota: Este método se usa principalmente para testing o casos especiales
     * ya que normalmente los achievements se crean desde el dominio
     * 
     * @param dto DTO de respuesta
     * @return Entidad de dominio
     */
    public static Achievement toDomain(AchievementResponseDto dto) {
        if (dto == null) {
            return null;
        }
        
        // Crear value objects
        var code = new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(dto.getCode());
        var title = new com.devmatch.api.achievement.domain.model.valueobject.AchievementTitle(dto.getTitle());
        var description = new com.devmatch.api.achievement.domain.model.valueobject.AchievementDescription(dto.getDescription());
        var points = new com.devmatch.api.achievement.domain.model.valueobject.AchievementPoints(dto.getPoints());
        var type = new com.devmatch.api.achievement.domain.model.valueobject.AchievementType(dto.getType());
        var icon = new com.devmatch.api.achievement.domain.model.valueobject.AchievementIcon(dto.getIconUrl());
        
        // Crear entidad usando el constructor de carga
        return new Achievement(
            code, title, description, points, type, icon,
            dto.isActive(), dto.isDeleted(), dto.getCreatedAt(), dto.getUpdatedAt()
        );
    }
}
