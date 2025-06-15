package com.devmatch.api.user.application.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequestDto {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El nombre de usuario solo puede contener letras, números, guiones y guiones bajos")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Email(message = "El email debe tener un formato válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El email debe tener un formato válido (ejemplo: usuario@dominio.com)")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "La contraseña debe contener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial (@#$%^&+=!)")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String lastName;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 60, message = "El país no puede exceder los 60 caracteres")
    private String country;

    @Size(max = 60, message = "La provincia no puede exceder los 60 caracteres")
    private String province;

    @Size(max = 60, message = "La ciudad no puede exceder los 60 caracteres")
    private String city;
} 