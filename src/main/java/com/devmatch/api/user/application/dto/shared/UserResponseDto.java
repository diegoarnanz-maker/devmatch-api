package com.devmatch.api.user.application.dto.shared;

import com.devmatch.api.user.domain.model.Role;
import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDto {
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
    private Set<Role> roles;
    private boolean active;
}
