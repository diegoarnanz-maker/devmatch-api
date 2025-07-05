package com.devmatch.api.user.application.dto.tag;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserTagRequestDto {
    @NotNull(message = "El ID del tag es requerido")
    private Long tagId;
} 