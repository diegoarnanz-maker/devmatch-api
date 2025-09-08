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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "profile-controller", description = "Endpoints para gestión de perfil de usuario")
public class ProfileController {

    private final ProfileUseCase profileUseCase;
    private final UserProfileTypeUseCase userProfileTypeUseCase;

    @Operation(summary = "Obtener mi perfil", description = "Obtiene el perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        return ResponseEntity.ok(profileUseCase.getMyProfile(userPrincipal.getUsername()));
    }

    @Operation(summary = "Obtener perfil público", description = "Obtiene el perfil de un usuario específico por ID (público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDto> getProfile(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(profileUseCase.getProfile(userId));
    }

    @Operation(summary = "Actualizar mi perfil", description = "Actualiza el perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Username o email ya en uso")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos de actualización del perfil")
            @Valid @RequestBody UserUpdateProfileRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.updateProfile(userPrincipal.getUserId(), dto));
    }

    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Contraseña actual incorrecta")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos para el cambio de contraseña")
            @Valid @RequestBody UserChangePasswordRequestDto dto) {
        profileUseCase.changePassword(userPrincipal.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar email", description = "Cambia el email del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email cambiado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "409", description = "El nuevo email ya está en uso")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile/email")
    public ResponseEntity<UserResponseDto> changeMyEmail(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos para el cambio de email")
            @Valid @RequestBody UserChangeEmailRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.changeEmail(userPrincipal.getUserId(), dto));
    }

    @Operation(summary = "Cambiar avatar", description = "Cambia el avatar del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avatar cambiado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile/avatar")
    public ResponseEntity<UserResponseDto> changeMyAvatar(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos para el cambio de avatar")
            @Valid @RequestBody UserChangeAvatarRequestDto dto) {
        return ResponseEntity.ok(profileUseCase.changeAvatar(userPrincipal.getUserId(), dto));
    }

    @Operation(summary = "Obtener mis tipos de perfil", description = "Obtiene los tipos de perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipos de perfil obtenidos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile/types")
    public ResponseEntity<List<ProfileTypeResponseDto>> getMyProfileTypes(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        return ResponseEntity.ok(userProfileTypeUseCase.getUserProfileTypes(userPrincipal.getUsername()));
    }

    @Operation(summary = "Agregar tipo de perfil", description = "Agrega un tipo de perfil al usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de perfil agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Tipo de perfil no encontrado"),
        @ApiResponse(responseCode = "409", description = "El tipo de perfil ya está asignado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/profile/types")
    public ResponseEntity<UserResponseDto> addProfileType(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos del tipo de perfil a agregar")
            @Valid @RequestBody UserProfileTypeRequestDto request) {
        return ResponseEntity.ok(userProfileTypeUseCase.addProfileType(userPrincipal.getUsername(), request));
    }

    @Operation(summary = "Remover tipo de perfil", description = "Remueve un tipo de perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de perfil removido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Tipo de perfil no encontrado en tu perfil")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/profile/types/{profileTypeId}")
    public ResponseEntity<UserResponseDto> removeProfileType(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID del tipo de perfil a remover", example = "1")
            @PathVariable Long profileTypeId) {
        return ResponseEntity.ok(userProfileTypeUseCase.removeProfileType(userPrincipal.getUsername(), profileTypeId));
    }
} 