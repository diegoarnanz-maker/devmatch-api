package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso UserQueryUseCase.
 * Este servicio maneja todas las operaciones de consulta relacionadas con usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserQueryUseCaseImpl implements UserQueryUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllActiveUsers() {
        return userRepositoryPort.findAllActive().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        return userRepositoryPort.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findUsersByStatus(String status) {
        List<User> users;
        
        if (status == null) {
            users = userRepositoryPort.findAll();
        } else {
            switch (status.toLowerCase()) {
                case "active":
                    users = userRepositoryPort.findAllActive();
                    break;
                case "inactive":
                    users = userRepositoryPort.findAll().stream()
                            .filter(user -> !user.isActive() && !user.isDeleted())
                            .collect(Collectors.toList());
                    break;
                case "deleted":
                    users = userRepositoryPort.findAll().stream()
                            .filter(User::isDeleted)
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new IllegalArgumentException("Estado no válido: " + status + ". Valores permitidos: active, inactive, deleted");
            }
        }
        
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findActiveUserByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .filter(User::isActive)
                .filter(user -> !user.isDeleted())
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findActiveUsersByRole(String roleName) {
        return userRepositoryPort.findAllActive().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().getValue().equals(roleName)))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUsernameExists(String username) {
        return userRepositoryPort.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailExists(String email) {
        return userRepositoryPort.existsByEmail(email);
    }
} 