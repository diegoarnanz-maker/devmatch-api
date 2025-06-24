package com.devmatch.api.role.application.mapper;

import com.devmatch.api.role.application.dto.RoleRequestDto;
import com.devmatch.api.role.application.dto.RoleResponseDto;
import com.devmatch.api.role.domain.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de roles
 */
@Component
public class RoleMapper {
    
    /**
     * Convierte un DTO de solicitud a una entidad de dominio
     * @param requestDto DTO de solicitud
     * @return Entidad de dominio
     */
    public Role toDomain(RoleRequestDto requestDto) {
        return new Role(requestDto.getName(), requestDto.getDescription());
    }
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta
     * @param role Entidad de dominio
     * @return DTO de respuesta
     */
    public RoleResponseDto toResponseDto(Role role) {
        return new RoleResponseDto(role.getId(), role.getName(), role.getDescription());
    }
    
    /**
     * Convierte una lista de entidades de dominio a una lista de DTOs de respuesta
     * @param roles Lista de entidades de dominio
     * @return Lista de DTOs de respuesta
     */
    public List<RoleResponseDto> toResponseDtoList(List<Role> roles) {
        return roles.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
} 