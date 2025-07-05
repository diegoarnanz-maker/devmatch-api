package com.devmatch.api.tag.application.dto;

import lombok.Data;

@Data
public class AdminTagResponseDto {
    private Long id;
    private String name;
    private String tagType;
    private boolean active;
} 