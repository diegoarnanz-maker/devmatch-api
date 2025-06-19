package com.devmatch.api.user.application.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRoleDto {
    
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(
        regexp = "^(USER|ADMIN)$",
        message = "El rol debe ser USER o ADMIN"
    )
    private String role;
}
