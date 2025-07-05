package com.devmatch.api.user.application.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminTagRequestDto {
    @NotBlank(message = "El nombre del tag es requerido")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "El tipo de tag es requerido")
    private String tagType;
} 