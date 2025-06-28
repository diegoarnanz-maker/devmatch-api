package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.admin.AdminProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.admin.AdminProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AdminProfileTypeUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.application.port.out.ProfileTypeRepositoryPort;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.exception.ProfileTypeInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProfileTypeUseCaseImpl implements AdminProfileTypeUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final ProfileTypeRepositoryPort profileTypeRepositoryPort;
    private final UserMapper userMapper;

    @Override
    public List<AdminProfileTypeResponseDto> getAllProfileTypes() {
        // Obtener todos los tipos de perfil del repositorio
        List<ProfileTypeRepositoryPort.ProfileTypeDto> profileTypes = profileTypeRepositoryPort.findAll();
        
        // Convertir a DTOs de respuesta
        return profileTypes.stream()
                .map(this::toAdminProfileTypeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminProfileTypeResponseDto createProfileType(AdminProfileTypeRequestDto request) {
        // Crear el tipo de perfil usando el repositorio
        ProfileTypeRepositoryPort.ProfileTypeDto createdProfileType = profileTypeRepositoryPort.create(
            request.getName(), 
            request.getDescription()
        );
        
        // Convertir a DTO de respuesta
        return toAdminProfileTypeResponseDto(createdProfileType);
    }

    @Override
    public AdminProfileTypeResponseDto updateProfileType(Long id, AdminProfileTypeRequestDto request) {
        // Actualizar el tipo de perfil usando el repositorio
        ProfileTypeRepositoryPort.ProfileTypeDto updatedProfileType = profileTypeRepositoryPort.update(
            id,
            request.getName(), 
            request.getDescription()
        );
        
        // Convertir a DTO de respuesta
        return toAdminProfileTypeResponseDto(updatedProfileType);
    }

    @Override
    public void deleteProfileType(Long id) {
        // Eliminar el tipo de perfil usando el repositorio
        // La excepción ProfileTypeInUseException se propagará directamente
        profileTypeRepositoryPort.delete(id);
    }

    @Override
    public List<ProfileTypeResponseDto> getUserProfileTypes(Long userId) {
        // Verificar que el usuario existe
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Obtener los tipos de perfil del usuario
        List<String> profileTypeNames = userRepositoryPort.findProfileTypesByUserId(userId);
        
        // Convertir a DTOs
        return profileTypeNames.stream()
                .map(this::createProfileTypeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto addProfileTypeToUser(Long userId, UserProfileTypeRequestDto request) {
        // Verificar que el usuario existe
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Verificar que el tipo de perfil existe
        profileTypeRepositoryPort.findById(request.getProfileTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de perfil no encontrado"));
        
        // Agregar el tipo de perfil al usuario
        profileTypeRepositoryPort.addProfileTypeToUser(userId, request.getProfileTypeId());
        
        // Obtener el usuario actualizado
        var updatedUser = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        return userMapper.toDto(updatedUser, userRepositoryPort.findProfileTypesByUserId(userId));
    }

    private ProfileTypeResponseDto createProfileTypeResponseDto(String profileTypeName) {
        ProfileTypeResponseDto dto = new ProfileTypeResponseDto();
        dto.setName(profileTypeName);
        // Los otros campos se pueden obtener del repositorio si es necesario
        return dto;
    }

    private AdminProfileTypeResponseDto toAdminProfileTypeResponseDto(ProfileTypeRepositoryPort.ProfileTypeDto dto) {
        AdminProfileTypeResponseDto responseDto = new AdminProfileTypeResponseDto();
        responseDto.setId(dto.getId());
        responseDto.setName(dto.getName());
        responseDto.setDescription(dto.getDescription());
        return responseDto;
    }
} 