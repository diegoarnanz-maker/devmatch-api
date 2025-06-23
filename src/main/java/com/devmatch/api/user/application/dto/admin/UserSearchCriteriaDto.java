package com.devmatch.api.user.application.dto.admin;

import lombok.Data;

@Data
public class UserSearchCriteriaDto {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String status; // Puede ser "active", "inactive", "deleted" o null
} 