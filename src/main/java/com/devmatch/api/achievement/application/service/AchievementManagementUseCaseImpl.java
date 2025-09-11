package com.devmatch.api.achievement.application.service;

import com.devmatch.api.achievement.application.port.in.AchievementManagementUseCase;
import com.devmatch.api.achievement.application.port.out.AchievementRepository;
import com.devmatch.api.achievement.application.dto.AchievementResponseDto;
import com.devmatch.api.achievement.application.mapper.AchievementMapper;
import com.devmatch.api.achievement.domain.model.Achievement;
import com.devmatch.api.achievement.domain.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso para gestión de logros.
 * 
 * <p>Este servicio implementa la lógica de negocio para operaciones de consulta
 * sobre el catálogo de logros. Proporciona acceso de solo lectura a logros activos
 * y es utilizado por usuarios autenticados y no autenticados.</p>
 * 
 * <h3>Responsabilidades:</h3>
 * <ul>
 *   <li>Consultar logros por ID o código</li>
 *   <li>Obtener catálogo completo de logros activos</li>
 *   <li>Filtrar logros por tipo</li>
 *   <li>Proporcionar paginación para grandes volúmenes</li>
 * </ul>
 * 
 * <h3>Flujo de trabajo:</h3>
 * <ol>
 *   <li>Recibe solicitudes de consulta</li>
 *   <li>Consulta repositorio de logros</li>
 *   <li>Aplica filtros de negocio (solo activos)</li>
 *   <li>Convierte entidades a DTOs</li>
 *   <li>Retorna datos al cliente</li>
 * </ol>
 * 
 * <h3>Consideraciones de negocio:</h3>
 * <ul>
 *   <li>Solo lectura: No modifica datos del sistema</li>
 *   <li>Filtros: Solo muestra logros activos y no eliminados</li>
 *   <li>Performance: Optimizado para consultas frecuentes</li>
 * </ul>
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementManagementUseCaseImpl implements AchievementManagementUseCase {
    
    private final AchievementRepository achievementRepository;
    
    @Override
    public AchievementResponseDto getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
            .orElseThrow(() -> new AchievementNotFoundException(id));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public AchievementResponseDto getAchievementByCode(String code) {
        Achievement achievement = achievementRepository.findByCode(code)
            .orElseThrow(() -> new AchievementNotFoundException(code));
        
        return AchievementMapper.toResponseDto(achievement);
    }
    
    @Override
    public List<AchievementResponseDto> getAllActiveAchievements() {
        List<Achievement> achievements = achievementRepository.findAllActive();
        return AchievementMapper.toResponseDtoList(achievements);
    }
    
    @Override
    public Page<AchievementResponseDto> getAllActiveAchievementsPaginated(Pageable pageable) {
        Page<Achievement> achievementsPage = achievementRepository.findAllActivePaginated(pageable);
        return achievementsPage.map(AchievementMapper::toResponseDto);
    }
    
    @Override
    public List<AchievementResponseDto> getAchievementsByType(String type) {
        List<Achievement> achievements = achievementRepository.findByType(type);
        return AchievementMapper.toResponseDtoList(achievements);
    }
}
