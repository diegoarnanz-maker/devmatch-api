package com.devmatch.api.user.infrastructure.in.controller;

import com.devmatch.api.user.application.dto.auth.JwtResponse;
import com.devmatch.api.user.application.dto.auth.LoginRequestDto;
import com.devmatch.api.user.application.dto.register.UserRegisterRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;
import com.devmatch.api.user.application.port.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequestDto loginRequest) {
        String token = authUseCase.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterRequestDto registerRequest) {
        UserResponseDto user = authUseCase.register(registerRequest);
        return ResponseEntity.ok(user);
    }
} 