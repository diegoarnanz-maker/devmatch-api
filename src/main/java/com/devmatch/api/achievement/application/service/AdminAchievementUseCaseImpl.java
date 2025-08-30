package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.dto.AdminAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminAchievementUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.mapper.AchievementMapper;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión administrativa de achievements.
 * Solo accesible por administradores.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminAchievementUseCaseImpl implements AdminAchievementUseCase {
    
    private final AchievementRepository achievementRepository;
    
    @Override
    public AchievementResponseDto createAchievement(AdminAchievementRequestDto request) {
        log.info("Admin creando achievement: {}", request.getCode());
        
        // Crear entidad de dominio
        Achievement achievement = new Achievement(
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(request.getCode()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementTitle(request.getTitle()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementDescription(request.getDescription()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementPoints(request.getPoints()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementType(request.getType()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementIcon(request.getIconUrl())
        );
        
        // Guardar en repositorio
        Achievement savedAchievement = achievementRepository.save(achievement);
        
        return AchievementMapper.toResponseDto(savedAchievement);
    }
    
    @Override
    public AchievementResponseDto updateAchievement(Long achievementId, AdminAchievementRequestDto request) {
        log.info("Admin actualizando achievement {}: {}", achievementId, request.getCode());
        
        Achievement existingAchievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        // Actualizar campos
        Achievement updatedAchievement = new Achievement(
            achievementId,
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(request.getCode()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementTitle(request.getTitle()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementDescription(request.getDescription()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementPoints(request.getPoints()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementType(request.getType()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementIcon(request.getIconUrl()),
            request.getIsActive(),
            existingAchievement.isDeleted(),
            existingAchievement.getCreatedAt(),
            LocalDateTime.now()
        );
        
        Achievement savedAchievement = achievementRepository.save(updatedAchievement);
        return AchievementMapper.toResponseDto(savedAchievement);
    }
    
    @Override
    public void deleteAchievement(Long achievementId) {
        log.info("Admin realizando soft delete del achievement: {}", achievementId);
        
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        // Soft delete - marcar como eliminado pero mantener datos
        // Los usuarios que ya lo tienen NO lo pierden, solo no pueden obtenerlo nuevos usuarios
        Achievement deletedAchievement = new Achievement(
            achievementId,
            achievement.getCode(),
            achievement.getTitle(),
            achievement.getDescription(),
            achievement.getPoints(),
            achievement.getType(),
            achievement.getIcon(),
            false, // isActive = false (no disponible para nuevos usuarios)
            true,  // isDeleted = true (marcado como eliminado)
            achievement.getCreatedAt(),
            LocalDateTime.now()
        );
        
        achievementRepository.save(deletedAchievement);
        
        log.info("Achievement {} marcado como eliminado. Los usuarios existentes mantienen su logro y puntos.", achievementId);
    }
    
    @Override
    public AchievementResponseDto toggleAchievementStatus(Long achievementId) {
        log.info("Admin cambiando estado del achievement: {}", achievementId);
        
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        // Toggle del estado
        boolean newStatus = !achievement.isActive();
        Achievement updatedAchievement = new Achievement(
            achievementId,
            achievement.getCode(),
            achievement.getTitle(),
            achievement.getDescription(),
            achievement.getPoints(),
            achievement.getType(),
            achievement.getIcon(),
            newStatus,
            achievement.isDeleted(),
            achievement.getCreatedAt(),
            LocalDateTime.now()
        );
        
        Achievement savedAchievement = achievementRepository.save(updatedAchievement);
        return AchievementMapper.toResponseDto(savedAchievement);
    }
    
    @Override
    public AchievementResponseDto getAchievementById(Long achievementId) {
        log.debug("Admin obteniendo achievement por ID: {}", achievementId);
        
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public Page<AchievementResponseDto> getAllAchievementsPaginated(Pageable pageable) {
        log.debug("Admin obteniendo todos los achievements (incluyendo eliminados)");
        
        // Para admin, mostrar TODOS los achievements (activos, inactivos y eliminados)
        // Como no tenemos findAll(Pageable), usamos findAll() y luego paginamos manualmente
        List<Achievement> allAchievements = achievementRepository.findAll();
        
        // Paginación manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allAchievements.size());
        
        if (start > allAchievements.size()) {
            return Page.empty(pageable);
        }
        
        List<Achievement> pageContent = allAchievements.subList(start, end);
        Page<Achievement> achievementsPage = new PageImpl<>(pageContent, pageable, allAchievements.size());
        
        return achievementsPage.map(AchievementMapper::toResponseDto);
    }
    
    @Override
    public List<AchievementResponseDto> getAchievementsByType(String type) {
        log.debug("Admin obteniendo achievements por tipo: {} (incluyendo eliminados)", type);
        
        // Para admin, mostrar TODOS los achievements del tipo (incluyendo eliminados)
        // Como findByType solo trae activos, usamos findAll y filtramos por tipo
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<Achievement> achievementsOfType = allAchievements.stream()
            .filter(achievement -> achievement.getType().getValue().equals(type))
            .collect(Collectors.toList());
        
        return AchievementMapper.toResponseDtoList(achievementsOfType);
    }
    
    @Override
    public AchievementResponseDto getAchievementByCode(String code) {
        log.debug("Admin obteniendo achievement por código: {}", code);
        
        Achievement achievement = achievementRepository.findByCode(code)
            .orElseThrow(() -> new AchievementNotFoundException(code));
        
        return AchievementMapper.toResponseDto(achievement);
    }
}
