package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.auth.JwtResponse;
import com.devmatch.api.user.application.dto.auth.LoginRequestDto;
import com.devmatch.api.user.application.dto.register.UserRegisterRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Tag(name = "auth-controller", description = "Endpoints de autenticación y registro de usuarios")
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve un token JWT para acceder a endpoints protegidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class),
                examples = @ExampleObject(
                    name = "Login exitoso",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwidXNlcklkIjoxLCJ1c2VybmFtZSI6InVzZXIxIiwiaWF0IjoxNzU3MjQ3NDU5LCJleHAiOjE4NDM2NDc0NTl9.example_token_here"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de autenticación",
                    value = """
                    {
                        "timestamp": "2025-09-08T10:00:00",
                        "status": 401,
                        "error": "Error de autenticación",
                        "message": "Credenciales inválidas"
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequestDto loginRequest) {
        String token = authUseCase.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario en la plataforma"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de registro inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                    {
                        "timestamp": "2025-09-08T10:00:00",
                        "status": 400,
                        "error": "Error de validación",
                        "message": "Los datos proporcionados no son válidos",
                        "details": {
                            "email": "El email ya está en uso",
                            "username": "El nombre de usuario ya existe"
                        }
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterRequestDto registerRequest) {
        UserResponseDto user = authUseCase.register(registerRequest);
        return ResponseEntity.ok(user);
    }
} 