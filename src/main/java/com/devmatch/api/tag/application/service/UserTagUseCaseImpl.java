package com.devmatch.api.tag.application.service;

import com.devmatch.api.tag.application.dto.TagResponseDto;
import com.devmatch.api.tag.application.dto.UserTagRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.tag.application.port.in.UserTagUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.tag.application.port.out.TagRepositoryPort;
import com.devmatch.api.user.application.mapper.UserMapper;
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
 * Implementación del caso de uso de gestión de tags de usuario.
 */
@Service
@RequiredArgsConstructor
public class UserTagUseCaseImpl implements UserTagUseCase {

    private final TagRepositoryPort tagRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepositoryPort.findAllActive();
        return tags.stream()
                .map(tagMapper::toResponseDto)
                .collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> searchTagsByName(String name) {
        List<Tag> tags = tagRepositoryPort.findByNameContaining(name);
        return tags.stream()
                .map(tagMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> getTagsByType(String tagType) {
        List<Tag> tags = tagRepositoryPort.findByTagType(tagType);
        return tags.stream()
                .map(tagMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> getUserTags(String username) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        return user.getTags().stream()
                .map(tagMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto addTag(String username, UserTagRequestDto request) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        Tag tag = tagRepositoryPort.findById(request.getTagId())
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado"));
        
        if (!tag.isActive()) {
            throw new TagNotFoundException("El tag no está activo");
        }
        
        // Verificar si el usuario ya tiene el tag
        if (user.hasTag(tag)) {
            throw new IllegalArgumentException("El usuario ya tiene este tag asignado");
        }
        
        user.addTag(tag);
        userRepositoryPort.save(user);
        
        // Recargar el usuario para obtener los tags actualizados
        User updatedUser = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        return userMapper.toDto(updatedUser, getProfileTypesForUser(updatedUser.getId()));
    }

    @Override
    @Transactional
    public UserResponseDto removeTag(String username, Long tagId) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        Tag tag = tagRepositoryPort.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag no encontrado"));
        
        // Verificar si el usuario tiene el tag
        if (!user.hasTag(tag)) {
            throw new IllegalArgumentException("El usuario no tiene este tag asignado");
        }
        
        user.removeTag(tag);
        userRepositoryPort.save(user);
        
        // Recargar el usuario para obtener los tags actualizados
        User updatedUser = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        return userMapper.toDto(updatedUser, getProfileTypesForUser(updatedUser.getId()));
    }
    
    /**
     * Método auxiliar para obtener los profile types de un usuario
     */
    private List<String> getProfileTypesForUser(Long userId) {
        return userRepositoryPort.findProfileTypesByUserId(userId)
                .stream()
                .map(UserRepositoryPort.ProfileTypeData::getName)
                .toList();
    }
} 