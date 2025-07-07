package com.devmatch.api.user.application.mapper;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.tag.application.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de usuario.
 * Este mapper solo trabaja con entidades de dominio, no con entidades JPA.
 */
@Component
public class UserMapper {

    @Autowired
    private TagMapper tagMapper;

    /**
     * Convierte una entidad User a UserResponseDto
     * 
     * @param user Entidad de dominio
     * @return DTO de respuesta
     */
    public UserResponseDto toDto(User user) {
        return toDto(user, null);
    }

    /**
     * Convierte una entidad User a UserResponseDto con profile types
     * 
     * @param user Entidad de dominio
     * @param profileTypes Lista de profile types del usuario
     * @return DTO de respuesta
     */
    public UserResponseDto toDto(User user, List<String> profileTypes) {
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
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setActive(user.isActive());
        dto.setDeleted(user.isDeleted());
        dto.setProfileTypes(profileTypes);
        
        // Mapear los tags del usuario
        if (user.getTags() != null) {
            dto.setTags(user.getTags().stream()
                    .map(tagMapper::toResponseDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}