package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserProfileResponseDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.ProfileUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.model.valueobject.user.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UserResponseDto updateProfile(Long userId, UserUpdateProfileRequestDto dto) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        user.updateProfile(
            dto.getFirstName(),
            dto.getLastName(),
            dto.getCountry(),
            dto.getProvince(),
            dto.getCity(),
            dto.getGithubUrl(),
            dto.getLinkedinUrl(),
            dto.getPortfolioUrl(),
            dto.getAvatarUrl(),
            dto.getBio()
        );

        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser);
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
    @Transactional(readOnly = true)
    public UserProfileResponseDto getProfile(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toProfileDto(user);
    }
} 