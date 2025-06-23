package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.auth.LoginRequestDto;
import com.devmatch.api.user.application.dto.register.UserRegisterRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.exception.AuthenticationException;
import com.devmatch.api.user.application.exception.UserAlreadyExistsException;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.AuthUseCase;
import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.security.application.port.out.AuthTokenRepositoryPort;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.model.Role;
import com.devmatch.api.user.domain.model.valueobject.user.Email;
import com.devmatch.api.user.domain.model.valueobject.user.Password;
import com.devmatch.api.user.domain.model.valueobject.user.Username;
import com.devmatch.api.user.domain.model.valueobject.role.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de autenticación y registro de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final AuthTokenRepositoryPort authTokenRepositoryPort;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String login(LoginRequestDto dto) {
        User user = userRepositoryPort.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthenticationException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash().getValue())) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        return authTokenRepositoryPort.generateToken(user);
    }

    @Override
    @Transactional
    public UserResponseDto register(UserRegisterRequestDto dto) {
        // Verificar si el usuario ya existe
        if (userRepositoryPort.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        if (userRepositoryPort.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

        Role userRole = new Role(new RoleName("USER"), "Usuario regular de la plataforma");
        User user = new User(
            new Username(dto.getUsername()),
            new Email(dto.getEmail()),
            new Password(passwordEncoder.encode(dto.getPassword())),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getCountry(),
            dto.getProvince(),
            dto.getCity(),
            null, // githubUrl
            null, // linkedinUrl
            null, // portfolioUrl
            null, // avatarUrl
            null, // bio
            userRole
        );

        // Guardar el usuario
        User savedUser = userRepositoryPort.save(user);
        return userMapper.toDto(savedUser);
    }
} 