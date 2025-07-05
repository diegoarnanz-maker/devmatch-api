package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.tag.AdminTagRequestDto;
import com.devmatch.api.user.application.dto.tag.AdminTagResponseDto;
import com.devmatch.api.user.application.dto.tag.TagResponseDto;
import com.devmatch.api.user.application.dto.tag.UserTagRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AdminTagUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.application.port.out.TagRepositoryPort;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.mapper.TagMapper;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.exception.TagNotFoundException;
import com.devmatch.api.user.domain.exception.TagInUseException;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.model.Tag;
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
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdminTagResponseDto> getAllTags() {
        List<Tag> tags = tagRepositoryPort.findAll();
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
        
        // Verificar si el tag está en uso
        if (tagRepositoryPort.isTagInUse(id)) {
            throw new TagInUseException("No se puede eliminar el tag porque está siendo utilizado por usuarios");
        }
        
        tag.markDeleted();
        tagRepositoryPort.save(tag);
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

    @Override
    @Transactional
    public UserResponseDto addTagToUser(Long userId, UserTagRequestDto request) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        Tag tag = tagRepositoryPort.findById(request.getTagId())
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado"));
        
        if (!tag.isActive()) {
            throw new TagNotFoundException("El tag no está activo");
        }
        
        user.addTag(tag);
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDto removeTagFromUser(Long userId, Long tagId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        Tag tag = tagRepositoryPort.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado"));
        
        user.removeTag(tag);
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser);
    }
} 