package com.devmatch.api.user.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String country;
    private final String province;
    private final String city;
    private final String bio;
    private final String githubUrl;
    private final String linkedinUrl;
    private final String portfolioUrl;
    private final String avatarUrl;
    private final boolean isActive;
} 