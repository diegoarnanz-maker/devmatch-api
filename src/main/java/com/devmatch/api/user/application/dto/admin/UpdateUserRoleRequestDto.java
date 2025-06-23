package com.devmatch.api.user.application.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequestDto {
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Pattern(regexp = "^(USER|ADMIN|MODERATOR)$", message = "El rol debe ser USER, ADMIN o MODERATOR")
    private String roleName;
} 