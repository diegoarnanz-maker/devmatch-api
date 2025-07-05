package com.devmatch.api.tag.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserTagRequestDto {
    @NotNull(message = "El ID del tag es requerido")
    private Long tagId;
} 