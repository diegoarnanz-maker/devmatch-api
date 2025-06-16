package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import com.devmatch.api.user.application.port.out.UserPersistencePort;
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
public class UserQueryService implements UserQueryUseCase {

    private final UserPersistencePort userPersistencePort;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllActiveUsers() {
        return userPersistencePort.findAllActive().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findActiveUserByUsername(String username) {
        return userPersistencePort.findByUsername(username)
                .filter(User::isActive)
                .filter(user -> !user.isDeleted())
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findActiveUsersByRole(String role) {
        return userPersistencePort.findAllActive().stream()
                .filter(user -> user.getRole().equals(role))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUsernameExists(String username) {
        return userPersistencePort.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailExists(String email) {
        return userPersistencePort.existsByEmail(email);
    }
} 