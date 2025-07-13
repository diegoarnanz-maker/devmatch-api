package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.profile.UserUpdateProfileRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangePasswordRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeEmailRequestDto;
import com.devmatch.api.user.application.dto.profile.UserChangeAvatarRequestDto;
import com.devmatch.api.user.application.dto.profile.ProfileTypeResponseDto;
import com.devmatch.api.user.application.dto.profile.UserProfileTypeRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.ProfileUseCase;
import com.devmatch.api.user.application.port.in.UserProfileTypeUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del perfil de usuario.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileUseCase profileUseCase;
    private final UserProfileTypeUseCase userProfileTypeUseCase;

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @return Perfil del usuario autenticado
     */
    @GetMapping("/profile/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        return ResponseEntity.ok(profileUseCase.getMyProfile(userPrincipal.getUsername()));
    }

    /**
     * Obtiene el perfil de un usuario específico por ID (público).
     *
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileUseCase.getProfile(userId));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param dto Datos de actualización del perfil
     * @return Usuario actualizado
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody UserUpdateProfileRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.updateProfile(userPrincipal.getUserId(), dto));
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param dto Datos para el cambio de contraseña
     * @return Respuesta vacía con código 204
     */
    @PutMapping("/profile/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody UserChangePasswordRequestDto dto) {
        profileUseCase.changePassword(userPrincipal.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambia el email del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param dto Datos para el cambio de email
     * @return Usuario actualizado
     */
    @PutMapping("/profile/email")
    public ResponseEntity<UserResponseDto> changeMyEmail(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody UserChangeEmailRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.changeEmail(userPrincipal.getUserId(), dto));
    }

    /**
     * Cambia el avatar del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param dto Datos para el cambio de avatar
     * @return Usuario actualizado
     */
    @PutMapping("/profile/avatar")
    public ResponseEntity<UserResponseDto> changeMyAvatar(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody UserChangeAvatarRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.changeAvatar(userPrincipal.getUserId(), dto));
    }

    /**
     * Obtiene los tipos de perfil del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @return Lista de tipos de perfil del usuario
     */
    @GetMapping("/profile/types")
    public ResponseEntity<List<ProfileTypeResponseDto>> getMyProfileTypes(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        return ResponseEntity.ok(userProfileTypeUseCase.getUserProfileTypes(userPrincipal.getUsername()));
    }

    /**
     * Agrega un tipo de perfil al usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param request DTO con el ID del tipo de perfil a agregar
     * @return Usuario actualizado
     */
    @PostMapping("/profile/types")
    public ResponseEntity<UserResponseDto> addProfileType(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody UserProfileTypeRequestDto request) {
        return ResponseEntity.ok(userProfileTypeUseCase.addProfileType(userPrincipal.getUsername(), request));
    }

    /**
     * Remueve un tipo de perfil del usuario autenticado.
     *
     * @param userPrincipal Usuario autenticado
     * @param profileTypeId ID del tipo de perfil a remover
     * @return Usuario actualizado
     */
    @DeleteMapping("/profile/types/{profileTypeId}")
    public ResponseEntity<UserResponseDto> removeProfileType(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long profileTypeId) {
        return ResponseEntity.ok(userProfileTypeUseCase.removeProfileType(userPrincipal.getUsername(), profileTypeId));
    }
} 