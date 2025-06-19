package com.devmatch.api.user.application.mapper;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de usuario.
 * Este mapper solo trabaja con entidades de dominio, no con entidades JPA.
 */
@Component
public class UserMapper {

    /**
     * Convierte una entidad User a UserResponseDto
     * 
     * @param user Entidad de dominio
     * @return DTO de respuesta
     */
    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername().getValue());
        dto.setEmail(user.getEmail().getValue());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCountry(user.getCountry());
        dto.setProvince(user.getProvince());
        dto.setCity(user.getCity());
        dto.setGithubUrl(user.getGithubUrl());
        dto.setLinkedinUrl(user.getLinkedinUrl());
        dto.setPortfolioUrl(user.getPortfolioUrl());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setRoles(
            user.getRoles().stream()
                .map(role -> role.getName().getValue())
                .collect(java.util.stream.Collectors.toSet())
        );
        dto.setActive(user.isActive());
        return dto;
    }
}