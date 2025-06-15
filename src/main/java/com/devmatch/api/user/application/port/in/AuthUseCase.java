package com.devmatch.api.user.application.port.in;

import com.devmatch.api.user.application.dto.auth.LoginRequestDto;
import com.devmatch.api.user.application.dto.register.UserRegisterRequestDto;
import com.devmatch.api.user.application.dto.shared.UserResponseDto;

/**
 * Casos de uso relacionados con la autenticación de usuarios.
 * 
 * Define las operaciones necesarias para permitir el inicio de sesión
 * y el registro de nuevos usuarios en la plataforma DevMatch.
 */
public interface AuthUseCase {

    /**
     * Autentica al usuario utilizando sus credenciales y devuelve un token JWT si
     * el login es exitoso.
     * 
     * @param dto Objeto que contiene el username/email y la contraseña.
     * @return Cadena JWT con la sesión del usuario autenticado.
     */
    String login(LoginRequestDto dto);

    /**
     * Registra un nuevo usuario en el sistema con los datos proporcionados.
     * 
     * @param dto Objeto con la información necesaria para registrar al usuario.
     * @return DTO con la información del usuario registrado.
     */
    UserResponseDto register(UserRegisterRequestDto dto);
}
