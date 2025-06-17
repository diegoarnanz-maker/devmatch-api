package com.devmatch.api.user.application.dto.profile;

import lombok.Data;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String province;
    private String city;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String avatarUrl;
    private String bio;
    private String role;
    private boolean isActive;
} 