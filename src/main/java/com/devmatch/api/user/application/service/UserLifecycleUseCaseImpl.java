package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.port.in.UserLifecycleUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.exception.UserNotFoundException;
import com.devmatch.api.user.domain.exception.UserOperationNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de gestión del ciclo de vida de usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserLifecycleUseCaseImpl implements UserLifecycleUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setActive(false);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void reactivateUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setActive(true);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setDeleted(true);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void restoreUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setDeleted(false);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void deleteIfNotAdmin(Long userId) {
        User user = findUserOrThrow(userId);
        if (user.isAdmin()) {
            throw new UserOperationNotAllowedException("No se puede eliminar un usuario administrador");
        }
        user.setDeleted(true);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = findUserOrThrow(userId);
        user.setActive(!user.isActive());
        userRepositoryPort.save(user);
    }

    private User findUserOrThrow(Long userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
    }
} 