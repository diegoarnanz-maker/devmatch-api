package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.dto.AdminAchievementRequestDto;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.port.in.AdminAchievementUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.mapper.AchievementMapper;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para gestión administrativa de logros.
 * 
 * <p>Este servicio implementa la lógica de negocio para operaciones administrativas
 * sobre logros del sistema. Solo es accesible por usuarios con rol de administrador
 * y maneja el ciclo de vida completo de los logros.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Crear nuevos logros en el catálogo</li>
 *   <li>Actualizar información de logros existentes</li>
 *   <li>Eliminar logros (soft delete)</li>
 *   <li>Activar/desactivar logros</li>
 *   <li>Consultar logros incluyendo eliminados</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Valida permisos de administrador</li>
 *   <li>Crea/actualiza entidades de dominio con value objects</li>
 *   <li>Persiste cambios en el repositorio</li>
 *   <li>Publica eventos de dominio si es necesario</li>
 *   <li>Retorna DTOs de respuesta</li>
 * </ol>
 * 
 * <h3>Consideraciones de negocio:</h3>
 * <ul>
 *   <li>Soft delete: Los usuarios existentes mantienen sus logros</li>
 *   <li>Validaciones: Códigos únicos, campos obligatorios</li>
 *   <li>Auditoría: Registro de fechas de creación y actualización</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminAchievementUseCaseImpl implements AdminAchievementUseCase {
    
    private final AchievementRepository achievementRepository;
    
    @Override
    public AchievementResponseDto createAchievement(AdminAchievementRequestDto request) {
        // Crear nuevo logro
        Achievement achievement = new Achievement(
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementCode(request.getCode()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementTitle(request.getTitle()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementDescription(request.getDescription()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementPoints(request.getPoints()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementType(request.getType()),
            new com.devmatch.api.achievement.domain.model.valueobject.AchievementIcon(request.getIconUrl())
        );
        
        Achievement savedAchievement = achievementRepository.save(achievement);
        
        return AchievementMapper.toResponseDto(savedAchievement);
    }
    
    @Override
    public AchievementResponseDto updateAchievement(Long achievementId, AdminAchievementRequestDto request) {
        // Buscar y actualizar logro existente
        Achievement existingAchievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
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
        // Soft delete del logro
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        Achievement deletedAchievement = new Achievement(
            achievementId,
            achievement.getCode(),
            achievement.getTitle(),
            achievement.getDescription(),
            achievement.getPoints(),
            achievement.getType(),
            achievement.getIcon(),
            false,
            true,
            achievement.getCreatedAt(),
            LocalDateTime.now()
        );
        
        achievementRepository.save(deletedAchievement);
        
    }
    
    @Override
    public AchievementResponseDto toggleAchievementStatus(Long achievementId) {
        // Cambiar estado activo/inactivo del logro
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
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
        // Obtener logro por ID
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new AchievementNotFoundException(achievementId));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public Page<AchievementResponseDto> getAllAchievementsPaginated(Pageable pageable) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        
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
    public Page<AchievementResponseDto> getAchievementsByTypePaginated(String type, Pageable pageable) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<Achievement> achievementsOfType = allAchievements.stream()
            .filter(achievement -> achievement.getType().getValue().equals(type))
            .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), achievementsOfType.size());
        
        if (start > achievementsOfType.size()) {
            return Page.empty(pageable);
        }
        
        List<Achievement> pageContent = achievementsOfType.subList(start, end);
        Page<Achievement> achievementsPage = new PageImpl<>(pageContent, pageable, achievementsOfType.size());
        
        return achievementsPage.map(AchievementMapper::toResponseDto);
    }
    
    @Override
    public AchievementResponseDto getAchievementByCode(String code) {
        // Obtener logro por código
        Achievement achievement = achievementRepository.findByCode(code)
            .orElseThrow(() -> new AchievementNotFoundException(code));
        
        return AchievementMapper.toResponseDto(achievement);
    }
}
