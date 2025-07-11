package com.devmatch.api.project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para solicitar cambios de rol de miembros del proyecto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRoleRequestDto {
    
    @NotBlank(message = "El rol no puede estar vacío")
    @Pattern(regexp = "^(LEADER|BACKEND|FRONTEND|FULLSTACK|UX_UI|QA|DEVOPS)$", 
             message = "El rol debe ser LEADER, BACKEND, FRONTEND, FULLSTACK, UX_UI, QA o DEVOPS")
    private String role;
} 