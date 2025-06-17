package com.devmatch.api.user.application.dto.profile;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateProfileRequestDto {
    
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String firstName;

    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String lastName;

    @Size(max = 60, message = "El país no puede exceder los 60 caracteres")
    private String country;

    @Size(max = 60, message = "La provincia no puede exceder los 60 caracteres")
    private String province;

    @Size(max = 60, message = "La ciudad no puede exceder los 60 caracteres")
    private String city;

    @Size(max = 1000, message = "La biografía no puede exceder los 1000 caracteres")
    private String bio;

    @Size(max = 255, message = "La URL de GitHub no puede exceder los 255 caracteres")
    private String githubUrl;

    @Size(max = 255, message = "La URL de LinkedIn no puede exceder los 255 caracteres")
    private String linkedinUrl;

    @Size(max = 255, message = "La URL del portfolio no puede exceder los 255 caracteres")
    private String portfolioUrl;

    @Size(max = 255, message = "La URL del avatar no puede exceder los 255 caracteres")
    private String avatarUrl;
} 