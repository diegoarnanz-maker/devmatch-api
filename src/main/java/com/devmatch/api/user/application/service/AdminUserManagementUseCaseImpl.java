package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.dto.admin.UpdateUserRoleRequestDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.AdminUserManagementUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.exception.UserOperationNotAllowedException;
import com.devmatch.api.role.domain.model.Role;
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
    @Transactional(readOnly = true)
    public UserResponseDto getUserDetailsForAdmin(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserRole(Long userId, UpdateUserRoleRequestDto request) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Crear el nuevo rol con el nombre proporcionado
        Role newRole = new Role(request.getRoleName(), "Rol asignado por administrador");
        user.setRole(newRole);
        
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserStatus(Long userId, boolean active) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        user.setActive(active);
        User updatedUser = userRepositoryPort.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Verificar que no sea admin
        if (user.isAdmin()) {
            throw new UserOperationNotAllowedException("No se puede eliminar un usuario administrador");
        }

        // Soft delete: marcar como eliminado y desactivar
        user.setDeleted(true);
        user.setActive(false);
        userRepositoryPort.save(user);
    }
} 