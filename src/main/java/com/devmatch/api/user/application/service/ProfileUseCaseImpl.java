package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeEmailRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeAvatarRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.ProfileUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.model.valueobject.user.Email;
import com.devmatch.api.user.domain.model.valueobject.user.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso de gestión del perfil de usuario.
 */
@Service
@RequiredArgsConstructor
public class ProfileUseCaseImpl implements ProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getMyProfile(String username) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user, getProfileTypesForUser(user.getId()));
    }

    @Override
    @Transactional
    public UserResponseDto updateProfile(Long userId, UserUpdateProfileRequestDto dto) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Solo actualizar campos que no sean null
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getCountry() != null) {
            user.setCountry(dto.getCountry());
        }
        if (dto.getProvince() != null) {
            user.setProvince(dto.getProvince());
        }
        if (dto.getCity() != null) {
            user.setCity(dto.getCity());
        }
        if (dto.getGithubUrl() != null) {
            user.setGithubUrl(dto.getGithubUrl());
        }
        if (dto.getLinkedinUrl() != null) {
            user.setLinkedinUrl(dto.getLinkedinUrl());
        }
        if (dto.getPortfolioUrl() != null) {
            user.setPortfolioUrl(dto.getPortfolioUrl());
        }
        if (dto.getAvatarUrl() != null) {
            user.setAvatarUrl(dto.getAvatarUrl());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }

        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser, getProfileTypesForUser(userId));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, UserChangePasswordRequestDto dto) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash().getValue())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        user.changePassword(new Password(passwordEncoder.encode(dto.getNewPassword())));
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public UserResponseDto changeEmail(Long userId, UserChangeEmailRequestDto dto) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Verificar que el email actual coincida
        if (!user.getEmail().getValue().equals(dto.getCurrentEmail())) {
            throw new IllegalArgumentException("El email actual no coincide");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash().getValue())) {
            throw new IllegalArgumentException("La contraseña es incorrecta");
        }

        // Verificar que el nuevo email no esté en uso
        userRepositoryPort.findByEmail(dto.getNewEmail())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("El nuevo email ya está en uso");
                });

        // Cambiar el email
        user.changeEmail(new Email(dto.getNewEmail()));
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser, getProfileTypesForUser(userId));
    }

    @Override
    @Transactional
    public UserResponseDto changeAvatar(Long userId, UserChangeAvatarRequestDto dto) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        user.setAvatarUrl(dto.getAvatarUrl());
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser, getProfileTypesForUser(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getProfile(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user, getProfileTypesForUser(userId));
    }

    /**
     * Método auxiliar para obtener los profile types de un usuario
     * TODO: Implementar consulta real a la base de datos
     */
    private List<String> getProfileTypesForUser(Long userId) {
        // Por ahora, hardcodeamos para el usuario 1 (BACKEND) y 2 (FULLSTACK)
        if (userId == 1L) {
            return List.of("BACKEND");
        } else if (userId == 2L) {
            return List.of("FULLSTACK");
        }
        return null;
    }
} 