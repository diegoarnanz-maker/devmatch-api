package com.devmatch.api.user.application.service;

import com.devmatch.api.user.application.dto.auth.LoginRequestDto;
import com.devmatch.api.user.application.dto.register.UserRegisterRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.mapper.UserMapper;
import com.devmatch.api.user.application.port.in.AuthUseCase;
import com.devmatch.api.user.application.port.out.AuthTokenPort;
import com.devmatch.api.user.application.port.out.UserPersistencePort;
import com.devmatch.api.user.domain.exception.AuthenticationException;
import com.devmatch.api.user.domain.exception.UserAlreadyExistsException;
import com.devmatch.api.user.domain.model.User;
import com.devmatch.api.user.domain.model.valueobject.Email;
import com.devmatch.api.user.domain.model.valueobject.Password;
import com.devmatch.api.user.domain.model.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que implementa la lógica de autenticación y registro de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserPersistencePort userPersistencePort;
    private final AuthTokenPort authTokenPort;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String login(LoginRequestDto dto) {
        User user = userPersistencePort.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthenticationException("Credenciales inválidas"));

        if (!user.isActive()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash().getValue())) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        return authTokenPort.generateToken(user);
    }

    @Override
    @Transactional
    public UserResponseDto register(UserRegisterRequestDto dto) {
        if (userPersistencePort.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        if (userPersistencePort.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

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
            null  // bio
        );

        User savedUser = userPersistencePort.save(user);
        return userMapper.toDto(savedUser);
    }
} 