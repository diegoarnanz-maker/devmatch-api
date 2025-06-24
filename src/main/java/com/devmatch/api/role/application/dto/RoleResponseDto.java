package com.devmatch.api.role.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de roles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {
    
    private Long id;
    private String name;
    private String description;
} 