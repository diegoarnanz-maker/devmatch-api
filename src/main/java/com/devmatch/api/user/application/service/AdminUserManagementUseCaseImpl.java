package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.exception.UserOperationNotAllowedException;
import com.devmatch.api.user.domain.model.Role;
import com.devmatch.api.user.domain.model.valueobject.role.RoleName;
import com.devmatch.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de gestión administrativa de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AdminUserManagementUseCaseImpl implements AdminUserManagementUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void updateUserRole(Long userId, String newRole) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        try {
            RoleName roleName = new RoleName(newRole);
            Role role = new Role(roleName, "Rol asignado por administrador");
            user.addRole(role);
            userRepositoryPort.save(user);
        } catch (IllegalArgumentException e) {
            throw new UserOperationNotAllowedException("Rol no válido: " + newRole);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserDetailsForAdmin(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user);
    }
} 