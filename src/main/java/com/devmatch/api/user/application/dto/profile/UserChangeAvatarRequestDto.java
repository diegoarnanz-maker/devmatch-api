package com.devmatch.api.user.application.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserChangeAvatarRequestDto {
    
    @NotBlank(message = "La URL del avatar es obligatoria")
    @Pattern(
        regexp = "^(https?://).*",
        message = "La URL del avatar debe comenzar con http:// o https://"
    )
    private String avatarUrl;
} 