package com.devmatch.api.user.application.mapper;

import com.devmatch.api.user.domain.model.Tag;
import com.devmatch.api.user.application.dto.tag.AdminTagResponseDto;
import com.devmatch.api.user.application.dto.tag.TagResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio Tag y DTOs.
 */
@Component
public class TagMapper {
    
    /**
     * Convierte una entidad Tag a TagResponseDto.
     *
     * @param tag Entidad de dominio
     * @return DTO de respuesta
     */
    public TagResponseDto toResponseDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        
        TagResponseDto dto = new TagResponseDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setTagType(tag.getTagType());
        return dto;
    }
    
    /**
     * Convierte una entidad Tag a AdminTagResponseDto.
     *
     * @param tag Entidad de dominio
     * @return DTO de respuesta administrativa
     */
    public AdminTagResponseDto toAdminResponseDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        
        AdminTagResponseDto dto = new AdminTagResponseDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setTagType(tag.getTagType());
        dto.setActive(tag.isActive());
        return dto;
    }
    
    /**
     * Convierte un DTO de solicitud a entidad Tag.
     *
     * @param name Nombre del tag
     * @param tagType Tipo del tag
     * @return Entidad de dominio
     */
    public Tag toDomain(String name, String tagType) {
        return new Tag(name, tagType);
    }
} 