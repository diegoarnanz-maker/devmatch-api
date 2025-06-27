package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.admin.UserSearchCriteriaDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.UserQueryUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public UserResponseDto findUserById(Long userId) {
        return userRepositoryPort.findById(userId)
                .map(user -> userMapper.toDto(user, getProfileTypesForUser(userId)))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    /**
     * Método auxiliar para obtener los profile types de un usuario
     */
    private List<String> getProfileTypesForUser(Long userId) {
        return userRepositoryPort.findProfileTypesByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(UserSearchCriteriaDto criteria, Pageable pageable) {
        return userRepositoryPort.searchUsers(criteria, pageable).map(userMapper::toDto);
    }
} 