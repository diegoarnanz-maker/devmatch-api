package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.UserProfileTypeUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.application.port.out.ProfileTypeRepositoryPort;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileTypeUseCaseImpl implements UserProfileTypeUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final ProfileTypeRepositoryPort profileTypeRepositoryPort;
    private final UserMapper userMapper;

    @Override
    public List<ProfileTypeResponseDto> getUserProfileTypes(String username) {
        // Obtener el usuario
        var user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Obtener los tipos de perfil del usuario
        List<UserRepositoryPort.ProfileTypeData> profileTypeData = userRepositoryPort.findProfileTypesByUserId(user.getId());
        
        // Convertir a DTOs
        return profileTypeData.stream()
                .map(this::createProfileTypeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto addProfileType(String username, UserProfileTypeRequestDto request) {
        // Obtener el usuario
        var user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Verificar que el tipo de perfil existe
        var profileType = profileTypeRepositoryPort.findById(request.getProfileTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de perfil no encontrado"));
        
        // Agregar el tipo de perfil al usuario
        profileTypeRepositoryPort.addProfileTypeToUser(user.getId(), request.getProfileTypeId());
        
        // Obtener el usuario actualizado con los nuevos tipos de perfil
        var updatedUser = userRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Obtener los tipos de perfil actualizados
        List<String> profileTypes = userRepositoryPort.findProfileTypesByUserId(user.getId())
                .stream()
                .map(UserRepositoryPort.ProfileTypeData::getName)
                .collect(Collectors.toList());
        
        return userMapper.toDto(updatedUser, profileTypes);
    }

    @Override
    public UserResponseDto removeProfileType(String username, Long profileTypeId) {
        // Obtener el usuario
        var user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Remover el tipo de perfil del usuario
        profileTypeRepositoryPort.removeProfileTypeFromUser(user.getId(), profileTypeId);
        
        // Obtener el usuario actualizado con los tipos de perfil actualizados
        var updatedUser = userRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Obtener los tipos de perfil actualizados
        List<String> profileTypes = userRepositoryPort.findProfileTypesByUserId(user.getId())
                .stream()
                .map(UserRepositoryPort.ProfileTypeData::getName)
                .collect(Collectors.toList());
        
        return userMapper.toDto(updatedUser, profileTypes);
    }

    private ProfileTypeResponseDto createProfileTypeResponseDto(UserRepositoryPort.ProfileTypeData profileTypeData) {
        ProfileTypeResponseDto dto = new ProfileTypeResponseDto();
        dto.setId(profileTypeData.getId());
        dto.setName(profileTypeData.getName());
        dto.setDescription("Descripción de " + profileTypeData.getName());
        return dto;
    }
} 