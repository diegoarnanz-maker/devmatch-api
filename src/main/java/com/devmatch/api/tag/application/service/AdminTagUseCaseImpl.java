package com.devmatch.api.tag.application.service;

import com.devmatch.api.tag.application.dto.AdminTagRequestDto;
import com.devmatch.api.tag.application.dto.AdminTagResponseDto;
import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.port.in.AdminTagUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.tag.application.port.out.TagRepositoryPort;
import com.devmatch.api.tag.application.mapper.TagMapper;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.tag.domain.exception.TagNotFoundException;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.tag.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso de gestión administrativa de tags.
 */
@Service
@RequiredArgsConstructor
public class AdminTagUseCaseImpl implements AdminTagUseCase {

    private final TagRepositoryPort tagRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdminTagResponseDto> getAllTags() {
        List<Tag> tags = tagRepositoryPort.findAll();
        return tags.stream()
                .map(tagMapper::toAdminResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTagResponseDto> getActiveTags() {
        List<Tag> tags = tagRepositoryPort.findAllActive();
        return tags.stream()
                .map(tagMapper::toAdminResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminTagResponseDto createTag(AdminTagRequestDto request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setTagType(request.getTagType());
        tag.setActive(true);
        
        Tag savedTag = tagRepositoryPort.save(tag);
        return tagMapper.toAdminResponseDto(savedTag);
    }

    @Override
    @Transactional
    public AdminTagResponseDto updateTag(Long id, AdminTagRequestDto request) {
        Tag tag = tagRepositoryPort.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado con ID: " + id));
        
        tag.setName(request.getName());
        tag.setTagType(request.getTagType());
        tag.updateTimestamp();
        
        Tag updatedTag = tagRepositoryPort.save(tag);
        return tagMapper.toAdminResponseDto(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepositoryPort.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado con ID: " + id));
        
        // Verificar si el tag está en uso para mostrar advertencia
        boolean isInUse = tagRepositoryPort.isTagInUse(id);
        
        if (isInUse) {
            // Log de advertencia para el administrador
            System.out.println("ADVERTENCIA: El tag '" + tag.getName() + "' está siendo utilizado por usuarios. Se procederá con la eliminación lógica.");
        }
        
        // Siempre permitir la eliminación lógica
        tag.markDeleted();
        tagRepositoryPort.save(tag);
    }

    @Override
    @Transactional
    public AdminTagResponseDto reactivateTag(Long id) {
        Tag tag = tagRepositoryPort.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado con ID: " + id));
        
        if (!tag.isDeleted()) {
            throw new IllegalArgumentException("El tag no está eliminado");
        }
        
        tag.setDeleted(false);
        tag.setActive(true);
        tag.updateTimestamp();
        
        Tag reactivatedTag = tagRepositoryPort.save(tag);
        return tagMapper.toAdminResponseDto(reactivatedTag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> getUserTags(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        return user.getTags().stream()
                .map(tagMapper::toResponseDto)
                .collect(Collectors.toList());
    }
} 