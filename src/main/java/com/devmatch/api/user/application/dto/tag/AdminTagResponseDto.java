package com.devmatch.api.user.application.dto.tag;

import lombok.Data;

@Data
public class AdminTagResponseDto {
    private Long id;
    private String name;
    private String tagType;
    private boolean active;
} 