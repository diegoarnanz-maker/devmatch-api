package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserProfileResponseDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.ProfileUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión del perfil de usuario.
 */
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileUseCase.getProfile(userId));
    }

    /**
     * Actualiza el perfil del usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos de actualización del perfil
     * @return Usuario actualizado
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateProfileRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.updateProfile(userId, dto));
    }

    /**
     * Cambia la contraseña del usuario.
     *
     * @param userId ID del usuario
     * @param dto Datos para el cambio de contraseña
     * @return Respuesta vacía con código 204
     */
    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserChangePasswordRequestDto dto) {
        profileUseCase.changePassword(userId, dto);
        return ResponseEntity.noContent().build();
    }
} 